package com.michael.base.position.dao.impl;

import com.michael.base.position.bo.PositionResourceBo;
import com.michael.base.position.dao.PositionResourceDao;
import com.michael.base.position.domain.PositionResource;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("positionResourceDao")
public class PositionResourceDaoImpl extends HibernateDaoHelper implements PositionResourceDao {

    @Override
    public String save(PositionResource positionResource) {
        return (String) getSession().save(positionResource);
    }

    @Override
    public void update(PositionResource positionResource) {
        getSession().update(positionResource);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PositionResource> query(PositionResourceBo bo) {
        Criteria criteria = createCriteria(PositionResource.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(PositionResourceBo bo) {
        Criteria criteria = createRowCountsCriteria(PositionResource.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + PositionResource.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(PositionResource positionResource) {
        Assert.notNull(positionResource, "要删除的对象不能为空!");
        getSession().delete(positionResource);
    }

    @Override
    public PositionResource findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (PositionResource) getSession().get(PositionResource.class, id);
    }

    @Override
    public void deleteByPosition(String positionId, String resourceType) {
        Assert.hasText(positionId, "删除失败!岗位ID不能为空!");
        Assert.hasText(positionId, "删除失败!资源类型不能为空!");
        getSession().createQuery("delete from " + PositionResource.class.getName() + " pr where pr.positionId=? and pr.resourceType=?")
                .setParameter(0, positionId)
                .setParameter(1, resourceType)
                .executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> queryByPosition(String positionId) {
        Assert.hasText(positionId, "查询失败!岗位ID不能为空个!");
        return createCriteria(PositionResource.class)
                .setProjection(Projections.property("resourceId"))
                .add(Restrictions.eq("positionId", positionId))
                .list();
    }

    private void initCriteria(Criteria criteria, PositionResourceBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}