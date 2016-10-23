package com.michael.base.position.dao.impl;

import com.michael.base.position.bo.PositionBo;
import com.michael.base.position.dao.PositionDao;
import com.michael.base.position.domain.Position;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.utils.NullUtils;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("positionDao")
public class PositionDaoImpl extends HibernateDaoHelper implements PositionDao {

    @Override
    public String save(Position position) {
        return (String) getSession().save(position);
    }

    @Override
    public void update(Position position) {
        getSession().update(position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Position> query(PositionBo bo) {
        Criteria criteria = createCriteria(Position.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.desc("sequenceNo"));
        return criteria.list();
    }

    @Override
    public Long getTotal(PositionBo bo) {
        Criteria criteria = createRowCountsCriteria(Position.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Position.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Position position) {
        Assert.notNull(position, "要删除的对象不能为空!");
        getSession().delete(position);
    }

    @Override
    public Position findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Position) getSession().get(Position.class, id);
    }

    @Override
    public boolean hasName(String name, String parentId, String id) {
        Assert.hasText(name, "查询名称是否重复失败!名称不能为空!");
        Criteria criteria = createRowCountsCriteria(Position.class);
        criteria.add(Restrictions.eq("name", name));
        if (StringUtils.isNotEmpty(parentId)) {
            criteria.add(Restrictions.eq("parentId", parentId));
        }
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        Long total = (Long) criteria.uniqueResult();
        return NullUtils.defaultValue(total, 0L) > 0;
    }

    @Override
    public boolean hasCode(String code, String id) {
        Assert.hasText(code, "查询编号是否重复失败!编号不能为空!");
        Criteria criteria = createRowCountsCriteria(Position.class);
        criteria.add(Restrictions.eq("code", code));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        Long total = (Long) criteria.uniqueResult();
        return NullUtils.defaultValue(total, 0L) > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Position> children(String id) {
        Assert.hasText(id, "查询子岗位失败!岗位ID不能为空!");
        return createCriteria(Position.class)
                .add(Restrictions.like("path", "%/" + id + "/%", MatchMode.ANYWHERE))
                .list();
    }

    private void initCriteria(Criteria criteria, PositionBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);

        // 根据路径进行查询
        if (bo != null && StringUtils.isNotEmpty(bo.getPath())) {
            criteria.add(Restrictions.like("path", "%/" + bo.getPath() + "/%"));
        }
    }

}