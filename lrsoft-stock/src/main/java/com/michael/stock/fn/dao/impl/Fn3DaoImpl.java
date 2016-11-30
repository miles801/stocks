package com.michael.stock.fn.dao.impl;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.stock.fn.bo.Fn3Bo;
import com.michael.stock.fn.dao.Fn3Dao;
import com.michael.stock.fn.domain.Fn3;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("fn3Dao")
public class Fn3DaoImpl extends HibernateDaoHelper implements Fn3Dao {

    @Override
    public String save(Fn3 fn3) {
        return (String) getSession().save(fn3);
    }

    @Override
    public void update(Fn3 fn3) {
        getSession().update(fn3);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Fn3> query(Fn3Bo bo) {
        Criteria criteria = createCriteria(Fn3.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Fn3> pageQuery(Fn3Bo bo) {
        Criteria criteria = createPagerCriteria(Fn3.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(Fn3Bo bo) {
        Criteria criteria = createRowCountsCriteria(Fn3.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Fn3.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Fn3 fn3) {
        Assert.notNull(fn3, "要删除的对象不能为空!");
        getSession().delete(fn3);
    }

    @Override
    public Fn3 findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Fn3) getSession().get(Fn3.class, id);
    }


    private void initCriteria(Criteria criteria, Fn3Bo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}