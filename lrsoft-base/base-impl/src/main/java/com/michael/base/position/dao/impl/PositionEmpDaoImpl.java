package com.michael.base.position.dao.impl;

import com.michael.base.position.bo.PositionEmpBo;
import com.michael.base.position.dao.PositionEmpDao;
import com.michael.base.position.domain.Position;
import com.michael.base.position.domain.PositionEmp;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("positionEmpDao")
public class PositionEmpDaoImpl extends HibernateDaoHelper implements PositionEmpDao {

    @Override
    public String save(PositionEmp positionEmp) {
        return (String) getSession().save(positionEmp);
    }

    @Override
    public void update(PositionEmp positionEmp) {
        getSession().update(positionEmp);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PositionEmp> query(PositionEmpBo bo) {
        Criteria criteria = createCriteria(PositionEmp.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(PositionEmpBo bo) {
        Criteria criteria = createRowCountsCriteria(PositionEmp.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + PositionEmp.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(PositionEmp positionEmp) {
        Assert.notNull(positionEmp, "要删除的对象不能为空!");
        getSession().delete(positionEmp);
    }

    @Override
    public PositionEmp find(String positionId, String empId) {
        Assert.hasText(positionId, "查询失败!岗位ID不能为空!");
        Assert.hasText(empId, "查询失败!员工ID不能为空!");
        return (PositionEmp) createCriteria(PositionEmp.class)
                .add(Restrictions.eq("positionId", positionId))
                .add(Restrictions.eq("empId", empId))
                .uniqueResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<String> queryEmp(String positionId) {
        Assert.hasText(positionId, "查询失败!岗位ID不能为空!");
        return createCriteria(PositionEmp.class)
                .setProjection(Projections.property("empId"))
                .add(Restrictions.eq("positionId", positionId))
                .list();
    }

    @Override
    public void deleteByEmp(String empId) {
        Assert.hasText(empId, "删除失败!员工ID不能为空!");
        getSession().createQuery("delete from " + PositionEmp.class.getName() + " pe where pe.empId=?")
                .setParameter(0, empId)
                .executeUpdate();
    }

    @Override
    public Long queryEmpTotal(String positionId) {
        Assert.hasText(positionId, "查询失败!岗位ID不能为空!");
        return (Long) createRowCountsCriteria(PositionEmp.class)
                .add(Restrictions.eq("positionId", positionId))
                .uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Position> queryByEmp(String empId) {
        Assert.hasText(empId, "岗位查询失败!员工ID不能为空!");
        DetachedCriteria findPositionIds = DetachedCriteria.forClass(PositionEmp.class)
                .setProjection(Projections.property("positionId"))
                .add(Restrictions.eq("empId", empId));

        return createCriteria(Position.class)
                .add(Property.forName("id").in(findPositionIds))
                .setFirstResult(0)
                .setMaxResults(Integer.MAX_VALUE)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> queryEmpPositionIds(String empId) {
        Assert.hasText(empId, "岗位查询失败!员工ID不能为空!");

        return createCriteria(PositionEmp.class)
                .setProjection(Projections.property("positionId"))
                .add(Restrictions.eq("empId", empId))
                .list();
    }

    @Override
    public PositionEmp findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (PositionEmp) getSession().get(PositionEmp.class, id);
    }

    private void initCriteria(Criteria criteria, PositionEmpBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}