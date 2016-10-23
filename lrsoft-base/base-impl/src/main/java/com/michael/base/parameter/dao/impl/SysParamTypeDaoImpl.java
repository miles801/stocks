package com.michael.base.parameter.dao.impl;

import com.michael.base.parameter.bo.SysParamTypeBo;
import com.michael.base.parameter.dao.SysParamTypeDao;
import com.michael.base.parameter.domain.SysParamType;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.core.pager.Pager;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author: miles
 * @datetime: 2014-06-20
 */
@Repository("sysParamTypeDao")
public class SysParamTypeDaoImpl extends HibernateDaoHelper implements SysParamTypeDao {

    @Override
    public String save(SysParamType sysParamType) {
        return (String) getSession().save(sysParamType);
    }

    @Override
    public void update(SysParamType sysParamType) {
        getSession().update(sysParamType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SysParamType> query(SysParamTypeBo bo) {
        Criteria criteria = getDefaultCriteria(bo);
        return criteria.list();
    }

    @Override
    public long getTotal(SysParamTypeBo bo) {
        Criteria criteria = createRowCountsCriteria(SysParamType.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void delete(SysParamType type) {
        Assert.notNull(type, "删除参数类型时,参数不能为空!");
        getSession().delete(type);
    }

    @Override
    public SysParamType findById(String id) {
        return (SysParamType) getSession().get(SysParamType.class, id);
    }

    /**
     * 获得默认的org.hibernate.Criteria对象,并根据bo进行初始化（如果bo为null，则会新建一个空对象）
     * 为了防止新的对象中有数据，建议实体/BO均采用封装类型
     */
    private Criteria getDefaultCriteria(SysParamTypeBo bo) {
        Criteria criteria = createCriteria(SysParamType.class);
        initCriteria(criteria, bo);
        // 默认排序规则：序号升序
        if (Pager.getOrder() == null || !Pager.getOrder().hasNext()) {
            criteria.addOrder(Order.asc("sequenceNo"));
        }
        return criteria;
    }

    @Override
    public String getName(String code) {
        Assert.hasText(code, "编号不能为空!");
        return (String) createCriteria(SysParamType.class)
                .setProjection(Projections.property("name"))
                .add(Restrictions.eq("code", code))
                .uniqueResult();
    }

    /**
     * 根据BO初始化org.hibernate.Criteria对象
     * 如果org.hibernate.Criteria为null，则抛出异常
     * 如果BO为null，则新建一个空的对象
     */
    private void initCriteria(Criteria criteria, SysParamTypeBo bo) {
        Assert.notNull(criteria, "Criteria不能为空!");
        CriteriaUtils.addCondition(criteria, bo);
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<SysParamType> queryChildren(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("查询孩子节点时,没有当前节点获得ID!");
        }
        return getDefaultCriteria(null)
                .add(Restrictions.like("path", "/" + id + "/", MatchMode.ANYWHERE))
                .add(Restrictions.ne("id", id))
                .list();
    }

    @Override
    public boolean hasCode(String code) {
        Assert.hasText(code, "查询系统参数类型的编号是否存在时,编号不能为空!");
        return (Long) createCriteria(SysParamType.class)
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("code", code))
                .uniqueResult() > 0;
    }

    @Override
    public boolean hasName(String parentId, String name) {
        Assert.hasText(name, "查询系统参数类型的名称是否存在时,名称不能为空!");
        Criteria criteria = createRowCountsCriteria(SysParamType.class);
        criteria.add(Restrictions.eq("name", name));
        if (parentId != null) {
            criteria.add(Restrictions.eq("parentId", parentId));
        }
        long total = (Long) criteria.uniqueResult();
        return total > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SysParamType> queryOther(String id) {
        Criteria criteria = getDefaultCriteria(new SysParamTypeBo());
        criteria.add(Restrictions.eq("status", "ACTIVE"));
        if (StringUtils.isEmpty(id)) {
            return criteria.list();
        }
        return criteria.add(Restrictions.ne("id", id))
                .add(Restrictions.not(Restrictions.like("path", id, MatchMode.ANYWHERE)))
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SysParamType> queryUsing() {
        return getDefaultCriteria(null)
                .add(Restrictions.in("status", new String[]{"ACTIVE", "CANCELED"}))
                .list();
    }
}