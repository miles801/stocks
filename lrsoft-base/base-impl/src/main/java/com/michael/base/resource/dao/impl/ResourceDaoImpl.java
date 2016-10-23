package com.michael.base.resource.dao.impl;

import com.michael.base.position.domain.PositionEmp;
import com.michael.base.position.domain.PositionResource;
import com.michael.base.resource.bo.ResourceBo;
import com.michael.base.resource.dao.ResourceDao;
import com.michael.base.resource.domain.Resource;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("resourceDao")
public class ResourceDaoImpl extends HibernateDaoHelper implements ResourceDao {

    @Override
    public String save(Resource resource) {
        return (String) getSession().save(resource);
    }

    @Override
    public void update(Resource resource) {
        getSession().update(resource);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Resource> query(ResourceBo bo) {
        Criteria criteria = createCriteria(Resource.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.asc("sequenceNo"));
        return criteria.list();
    }

    @Override
    public Long getTotal(ResourceBo bo) {
        Criteria criteria = createRowCountsCriteria(Resource.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Resource.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Resource resource) {
        Assert.notNull(resource, "要删除的对象不能为空!");
        getSession().delete(resource);
    }

    @Override
    public boolean hasCode(String code, String id) {
        Assert.hasText(code, "查询失败!资源编号不能为空!");
        Criteria criteria = createRowCountsCriteria(Resource.class)
                .add(Restrictions.eq("code", code));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        return (Long) criteria.uniqueResult() > 0;
    }

    @Override
    public boolean hasName(String name, String parentId, String id) {
        Assert.hasText(name, "查询失败!资源名称不能为空!");
        Criteria criteria = createRowCountsCriteria(Resource.class)
                .add(Restrictions.eq("name", name));
        if (StringUtils.isNotEmpty(parentId)) {
            criteria.add(Restrictions.eq("parentId", parentId));
        }
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        return (Long) criteria.uniqueResult() > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Resource> queryChildren(String parentId) {
        Criteria criteria = createCriteria(Resource.class);
        if (StringUtils.isEmpty(parentId)) {
            criteria.add(Restrictions.isNull("parentId"));
        } else {
            criteria.add(Restrictions.eq("parentId", parentId));
        }
        return criteria.list();
    }

    @Override
    public Resource findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Resource) getSession().get(Resource.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Resource> permissionResource(String empId, ResourceBo bo) {
        Assert.hasText(empId, "资源查询失败!员工ID不能为空!");
        Criteria criteria = createCriteria(Resource.class);
        CriteriaUtils.addCondition(criteria, bo);
        // 关联了 岗位员工表、岗位资源表、资源表
        DetachedCriteria pr = DetachedCriteria.forClass(PositionResource.class)
                .setProjection(Projections.property("resourceId"))
                .add(Property.forName("positionId").in(
                        DetachedCriteria.forClass(PositionEmp.class)
                                .setProjection(Projections.property("positionId"))
                                .add(Restrictions.eq("empId", empId)))
                );
        // 查询不需要授权的资源
        Criterion noAuth = Property.forName("id").in(
                DetachedCriteria.forClass(Resource.class)
                        .setProjection(Projections.property("id"))
                        .add(Restrictions.eq("authorization", false))
        );
        criteria.add(Restrictions.or(Property.forName("id").in(pr), noAuth));
        criteria.addOrder(Order.asc("sequenceNo"));
        return criteria.list();
    }

    @Override
    public List<String> permissionResourceCode(String empId, ResourceBo bo) {
        Assert.hasText(empId, "资源查询失败!员工ID不能为空!");
        Criteria criteria = createCriteria(Resource.class)
                .setProjection(Projections.property("code"));
        CriteriaUtils.addCondition(criteria, bo);
        // 关联了 岗位员工表、岗位资源表、资源表
        DetachedCriteria pr = DetachedCriteria.forClass(PositionResource.class)
                .setProjection(Projections.property("resourceId"))
                .add(Property.forName("positionId").in(
                        DetachedCriteria.forClass(PositionEmp.class)
                                .setProjection(Projections.property("positionId"))
                                .add(Restrictions.eq("empId", empId)))
                );
        // 查询不需要授权的资源
        Criterion noAuth = Property.forName("id").in(
                DetachedCriteria.forClass(Resource.class)
                        .setProjection(Projections.property("id"))
                        .add(Restrictions.eq("authorization", false))
        );
        criteria.add(Restrictions.or(Property.forName("id").in(pr), noAuth));
        return criteria.list();
    }

    private void initCriteria(Criteria criteria, ResourceBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}