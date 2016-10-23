package com.michael.base.parameter.dao.impl;

import com.michael.base.parameter.bo.BusinessParamItemBo;
import com.michael.base.parameter.dao.BusinessParamItemDao;
import com.michael.base.parameter.domain.BusinessParamItem;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.core.pager.Pager;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;


/**
 * @author: miles
 * @datetime: 2014-07-02
 */
@Repository("businessParamItemDao")
public class BusinessParamItemDaoImpl extends HibernateDaoHelper implements BusinessParamItemDao {

    @Override
    public String save(BusinessParamItem businessParamItem) {
        return (String) getSession().save(businessParamItem);
    }

    @Override
    public void update(BusinessParamItem businessParamItem) {
        getSession().update(businessParamItem);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<BusinessParamItem> query(BusinessParamItemBo bo) {
        Criteria criteria = getDefaultCriteria(bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(BusinessParamItemBo bo) {
        Criteria criteria = createRowCountsCriteria(BusinessParamItem.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public BusinessParamItem findById(String id) {
        return (BusinessParamItem) getSession().get(BusinessParamItem.class, id);
    }

    /**
     * 获得默认的org.hibernate.Criteria对象,并根据bo进行初始化（如果bo为null，则会新建一个空对象）
     * 为了防止新的对象中有数据，建议实体/BO均采用封装类型
     */
    private Criteria getDefaultCriteria(BusinessParamItemBo bo) {
        Criteria criteria = createCriteria(BusinessParamItem.class);
        initCriteria(criteria, bo);
        if (Pager.getOrder() == null || !Pager.getOrder().hasNext()) {
            criteria.addOrder(Order.asc("sequenceNo"));
        }
        return criteria;
    }

    /**
     * 根据BO初始化org.hibernate.Criteria对象
     * 如果org.hibernate.Criteria为null，则抛出异常
     * 如果BO为null，则新建一个空的对象
     */
    private void initCriteria(Criteria criteria, BusinessParamItemBo bo) {
        Assert.notNull(criteria, "Criteria不能为空!");
        CriteriaUtils.addCondition(criteria, bo);
    }

    @Override
    public boolean hasValue(String typeCode, String value) {
        if (StringUtils.hasEmpty(typeCode, value)) {
            throw new IllegalArgumentException("查询参数选项的值是否重复时,参数个数不匹配!");
        }
        Criteria criteria = createRowCountsCriteria(BusinessParamItem.class);
        long total = (Long) criteria.add(Restrictions.eq("type", typeCode))
                .add(Restrictions.eq("value", value))
                .uniqueResult();
        return total > 0;
    }

    @Override
    public void delete(BusinessParamItem item) {
        if (item == null) {
            throw new IllegalArgumentException("删除参数选项时,参数不能为空!");
        }
        getSession().delete(item);
    }

    @Override
    public boolean hasName(String typeCode, String name) {
        if (StringUtils.hasEmpty(typeCode, name)) {
            throw new IllegalArgumentException("查询参数选项的名称是否重复时,参数个数不匹配!");
        }
        Criteria criteria = createRowCountsCriteria(BusinessParamItem.class);
        long total = (Long) criteria.add(Restrictions.eq("type", typeCode))
                .add(Restrictions.eq("value", name))
                .uniqueResult();
        return total > 0;
    }

    @Override
    public String queryName(String type, String value) {
        Assert.hasText(type, "业务参数编号不能为空!");
        Assert.hasText(value, "业务参数的值不能为空!");
        return (String) createCriteria(BusinessParamItem.class)
                .setProjection(Projections.property("name"))
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("value", value))
                .uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<BusinessParamItem> fetchCascade(String typeCode, String value) {
        if (org.apache.commons.lang3.StringUtils.isAnyEmpty(typeCode, value)) {
            return null;
        }
        return getSession().createCriteria(BusinessParamItem.class)
                .add(Restrictions.eq("cascadeTypeCode", typeCode))
                .add(Restrictions.eq("cascadeItemValue", value))
                .addOrder(Order.asc("sequenceNo"))
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> hasBeenCascaded(Set<String> types, Set<String> codes) {

        if (types == null || types.isEmpty() || codes == null || codes.isEmpty()) {
            return null;
        }
        // 获得被级联的参数的值
        DetachedCriteria valueCriteria = DetachedCriteria.forClass(BusinessParamItem.class);
        valueCriteria.setProjection(Projections.distinct(Projections.property("cascadeItemValue")))
                .add(Restrictions.in("cascadeTypeCode", types))
                .add(Restrictions.in("cascadeItemValue", codes));

        // 查询被级联的参数对应的id
        return createCriteria(BusinessParamItem.class)
                .setProjection(Projections.distinct(Projections.id()))
                .add(Restrictions.in("type", types))
                .add(Property.forName("value").in(valueCriteria))
                .list();
    }

    /**
     * 通过业务参数的Name找到对应的业务参数的Value
     *
     * @param name
     * @return
     */
    @Override
    public BusinessParamItem findValueByName(String type, String name) {
        Criteria criteria = createCriteria(BusinessParamItem.class);
        criteria.add(Restrictions.eq("type", type)).add(Restrictions.eq("name", name));
        return (BusinessParamItem) criteria.uniqueResult();
    }

    @Override
    public void deleteByType(String type) {
        Assert.hasText(type, "根据类型编号删除参数失败!类型编号不能为空!");
        getSession().createQuery("delete from " + BusinessParamItem.class.getName() + " n where n.type=?")
                .setParameter(0, type)
                .executeUpdate();
    }
}