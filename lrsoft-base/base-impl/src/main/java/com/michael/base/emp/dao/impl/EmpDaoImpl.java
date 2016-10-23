package com.michael.base.emp.dao.impl;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.base.position.domain.PositionEmp;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Michael
 */
@Repository("empDao")
public class EmpDaoImpl extends HibernateDaoHelper implements EmpDao {

    @Override
    public String save(Emp emp) {
        return (String) getSession().save(emp);
    }

    @Override
    public void update(Emp emp) {
        getSession().update(emp);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Emp> query(EmpBo bo) {
        Criteria criteria = createCriteria(Emp.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    @Override
    public Long getTotal(EmpBo bo) {
        Criteria criteria = createRowCountsCriteria(Emp.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Emp.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Emp emp) {
        Assert.notNull(emp, "要删除的对象不能为空!");
        getSession().delete(emp);
    }

    @Override
    public String findOrgId(String id) {
        Assert.hasText(id, "查询员工的机构失败!员工ID不能为空!");
        return (String) createCriteria(Emp.class)
                .setProjection(Projections.property("orgId"))
                .add(Restrictions.idEq(id))
                .uniqueResult();
    }

    @Override
    public Emp findByLoginName(String loginName) {
        Assert.hasText(loginName, "查询用户失败!登录名不能为空!");
        return (Emp) createCriteria(Emp.class)
                .add(Restrictions.eq("loginName", loginName))
                .uniqueResult();
    }

    @Override
    public void updatePwd(String empId, String newPwd) {
        Assert.hasText(empId, "更新密码失败!员工ID不能为空!");
        Assert.hasText(newPwd, "更新密码失败!密码不能为空!");
        getSession().createQuery("update " + Emp.class.getName() + " e set e.password=? where e.id=?")
                .setParameter(0, newPwd)
                .setParameter(1, empId)
                .executeUpdate();
    }

    @Override
    public Emp findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Emp) getSession().get(Emp.class, id);
    }

    private void initCriteria(Criteria criteria, EmpBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");

        if (bo == null) {
            return;
        }
        CriteriaUtils.addCondition(criteria, bo);
        if (StringUtils.isNotEmpty(bo.getKeywords())) {
            String keywords = bo.getKeywords();
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.like("name", keywords, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.like("code", keywords, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.like("pinyin", keywords, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.like("phone", keywords, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.like("mobile", keywords, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.like("orgName", keywords, MatchMode.ANYWHERE));
            criteria.add(disjunction);
        }

        if (StringUtils.isNotEmpty(bo.getCreatorId())) {
            // 查询所有的创建信息
            List<String> ids = new ArrayList<>();
            ids.add("-1");
            masterEmpIds(ids, bo.getCreatorId());
            criteria.add(Restrictions.in("id", ids));
        }


    }

    @SuppressWarnings("unchecked")
    private List<String> masterEmpIds(List<String> empIds, String empId) {
        List<String> ids = getSession().createQuery("select e.id from " + Emp.class.getName() + " e where e.creatorId=?")
                .setParameter(0, empId)
                .list();
        if (ids != null && !ids.isEmpty()) {
            for (String id : ids) {
                if (!empIds.contains(id)) {
                    empIds.add(id);
                    masterEmpIds(empIds, id);
                }
            }
        }
        return empIds;
    }

    @Override
    public Long getTotalByOrg(String orgId, EmpBo bo) {
        Assert.hasText(orgId, "查询失败!组织机构ID不能为空!");
        Criteria criteria = createRowCountsCriteria(Emp.class);
        criteria.add(Restrictions.eq("orgId", orgId));
        CriteriaUtils.addCondition(criteria, bo);
        return (Long) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Emp> queryByOrg(String orgId, EmpBo bo) {
        Assert.hasText(orgId, "查询失败!组织机构ID不能为空!");
        Criteria criteria = createCriteria(Emp.class);
        criteria.add(Restrictions.eq("orgId", orgId));
        CriteriaUtils.addCondition(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotalByPosition(String positionId, EmpBo bo) {
        Assert.hasText(positionId, "查询失败!岗位ID不能为空!");
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PositionEmp.class)
                .setProjection(Projections.property("empId"))
                .add(Restrictions.eq("positionId", positionId));
        Criteria criteria = createRowCountsCriteria(Emp.class);
        CriteriaUtils.addCondition(criteria, bo);
        criteria.add(Property.forName("id").in(detachedCriteria));
        return (Long) criteria.uniqueResult();
    }


    @Override
    public boolean hasCode(String code, String id) {
        Assert.hasText(code, "验证编号失败!工号不能为空!");
        Criteria criteria = createRowCountsCriteria(Emp.class)
                .add(Restrictions.eq("code", code));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        return (Long) criteria.uniqueResult() > 0;
    }

    @Override
    public boolean hasLoginName(String loginName, String id) {
        Assert.hasText(loginName, "验证登录名失败!登录名不能为空!");
        Criteria criteria = createRowCountsCriteria(Emp.class)
                .add(Restrictions.eq("loginName", loginName));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        return (Long) criteria.uniqueResult() > 0;
    }

    @Override
    public boolean hasAttNo(String attNo, String id) {
        Assert.hasText(attNo, "验证失败考勤编号不能为空!");
        Criteria criteria = createRowCountsCriteria(Emp.class)
                .add(Restrictions.eq("attendanceNo", attNo));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        return (Long) criteria.uniqueResult() > 0;
    }

    @Override
    public Emp findByCode(String code) {
        Assert.hasText(code, "查询失败!员工编号不能为空!");
        return (Emp) createCriteria(Emp.class)
                .add(Restrictions.eq("loginName", code))
                .uniqueResult();
    }

    /**
     * 找到员工工号的最大的员工
     *
     * @return
     */
    @Override
    public Emp findMaxEmpCode() {
        String hql = "from  " + Emp.class.getName() + " e order by e.code DESC ";
        List<Emp> list = this.getSession().createQuery(hql).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Emp> queryByPosition(String positionId, EmpBo bo) {
        Assert.hasText(positionId, "查询失败!岗位ID不能为空!");
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PositionEmp.class)
                .setProjection(Projections.property("empId"))
                .add(Restrictions.eq("positionId", positionId));
        Criteria criteria = createCriteria(Emp.class);
        CriteriaUtils.addCondition(criteria, bo);
        criteria.add(Property.forName("id").in(detachedCriteria));
        return criteria.list();
    }


    @Override
    public List getAllCode() {
        return getSession().createQuery("FROM " + Emp.class.getName()).list();
    }
}