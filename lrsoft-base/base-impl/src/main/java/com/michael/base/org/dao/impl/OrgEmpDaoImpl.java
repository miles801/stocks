package com.michael.base.org.dao.impl;

import com.michael.base.emp.domain.Emp;
import com.michael.base.org.bo.OrgEmpBo;
import com.michael.base.org.dao.OrgEmpDao;
import com.michael.base.org.domain.OrgEmp;
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
@Repository("orgEmpDao")
public class OrgEmpDaoImpl extends HibernateDaoHelper implements OrgEmpDao {

    @Override
    public String save(OrgEmp orgEmp) {
        return (String) getSession().save(orgEmp);
    }

    @Override
    public void update(OrgEmp orgEmp) {
        getSession().update(orgEmp);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrgEmp> query(OrgEmpBo bo) {
        Criteria criteria = createCriteria(OrgEmp.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(OrgEmpBo bo) {
        Criteria criteria = createRowCountsCriteria(OrgEmp.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + OrgEmp.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(OrgEmp orgEmp) {
        Assert.notNull(orgEmp, "要删除的对象不能为空!");
        getSession().delete(orgEmp);
    }

    @Override
    public OrgEmp findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (OrgEmp) getSession().get(OrgEmp.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Emp> findByOrg(String orgId) {
        Assert.hasText(orgId, "查询失败,组织机构ID不能为空!");
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OrgEmp.class)
                .setProjection(Projections.property("empId"))
                .add(Restrictions.eq("orgId", orgId));
        return createCriteria(Emp.class)
                .add(Property.forName("id").in(detachedCriteria))
                .list();
    }

    @Override
    public OrgEmp find(String orgId, String empId) {
        Assert.hasText(orgId, "查询失败,组织机构ID不能为空!");
        Assert.hasText(empId, "查询失败,员工ID不能为空!");
        return (OrgEmp) createCriteria(OrgEmp.class)
                .add(Restrictions.eq("orgId", orgId))
                .add(Restrictions.eq("empId", empId))
                .uniqueResult();
    }

    private void initCriteria(Criteria criteria, OrgEmpBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}