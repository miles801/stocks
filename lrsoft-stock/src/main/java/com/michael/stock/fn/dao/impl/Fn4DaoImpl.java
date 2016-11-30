package com.michael.stock.fn.dao.impl;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.stock.fn.bo.Fn4Bo;
import com.michael.stock.fn.dao.Fn4Dao;
import com.michael.stock.fn.domain.Fn4;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("fn4Dao")
public class Fn4DaoImpl extends HibernateDaoHelper implements Fn4Dao {

    @Override
    public String save(Fn4 fn4) {
        return (String) getSession().save(fn4);
    }

    @Override
    public void update(Fn4 fn4) {
        getSession().update(fn4);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Fn4> query(Fn4Bo bo) {
        Criteria criteria = createCriteria(Fn4.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Fn4> pageQuery(Fn4Bo bo) {
        Criteria criteria = createPagerCriteria(Fn4.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(Fn4Bo bo) {
        Criteria criteria = createRowCountsCriteria(Fn4.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Fn4.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Fn4 fn4) {
        Assert.notNull(fn4, "要删除的对象不能为空!");
        getSession().delete(fn4);
    }

    @Override
    public Fn4 findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Fn4) getSession().get(Fn4.class, id);
    }


    private void initCriteria(Criteria criteria, Fn4Bo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}