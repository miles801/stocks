package com.michael.stock.fn.dao.impl;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.stock.fn.bo.Fn5Bo;
import com.michael.stock.fn.dao.Fn5Dao;
import com.michael.stock.fn.domain.Fn5;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("fn5Dao")
public class Fn5DaoImpl extends HibernateDaoHelper implements Fn5Dao {

    @Override
    public String save(Fn5 fn5) {
        return (String) getSession().save(fn5);
    }

    @Override
    public void update(Fn5 fn5) {
        getSession().update(fn5);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Fn5> query(Fn5Bo bo) {
        Criteria criteria = createCriteria(Fn5.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Fn5> pageQuery(Fn5Bo bo) {
        Criteria criteria = createPagerCriteria(Fn5.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(Fn5Bo bo) {
        Criteria criteria = createRowCountsCriteria(Fn5.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Fn5.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Fn5 fn5) {
        Assert.notNull(fn5, "要删除的对象不能为空!");
        getSession().delete(fn5);
    }

    @Override
    public Fn5 findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Fn5) getSession().get(Fn5.class, id);
    }


    private void initCriteria(Criteria criteria, Fn5Bo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}