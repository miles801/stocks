package com.michael.base.log.dao.impl;

import com.michael.base.log.bo.LoginLogBo;
import com.michael.base.log.dao.LoginLogDao;
import com.michael.base.log.domain.LoginLog;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("loginLogDao")
public class LoginLogDaoImpl extends HibernateDaoHelper implements LoginLogDao {

    @Override
    public String save(LoginLog loginLog) {
        return (String) getSession().save(loginLog);
    }

    @Override
    public void update(LoginLog loginLog) {
        getSession().update(loginLog);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LoginLog> query(LoginLogBo bo) {
        Criteria criteria = createCriteria(LoginLog.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LoginLog> pageQuery(LoginLogBo bo) {
        Criteria criteria = createPagerCriteria(LoginLog.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(LoginLogBo bo) {
        Criteria criteria = createRowCountsCriteria(LoginLog.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + LoginLog.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(LoginLog loginLog) {
        Assert.notNull(loginLog, "要删除的对象不能为空!");
        getSession().delete(loginLog);
    }

    @Override
    public LoginLog findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (LoginLog) getSession().get(LoginLog.class, id);
    }


    private void initCriteria(Criteria criteria, LoginLogBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}