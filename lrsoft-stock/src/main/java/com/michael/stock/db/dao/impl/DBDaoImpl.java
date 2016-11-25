package com.michael.stock.db.dao.impl;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.stock.db.bo.DBBo;
import com.michael.stock.db.dao.DBDao;
import com.michael.stock.db.domain.DB;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("dBDao")
public class DBDaoImpl extends HibernateDaoHelper implements DBDao {

    @Override
    public String save(DB dB) {
        return (String) getSession().save(dB);
    }

    @Override
    public void update(DB dB) {
        getSession().update(dB);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DB> query(DBBo bo) {
        Criteria criteria = createCriteria(DB.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.asc("dbDate"));
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DB> pageQuery(DBBo bo) {
        Criteria criteria = createPagerCriteria(DB.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(DBBo bo) {
        Criteria criteria = createRowCountsCriteria(DB.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + DB.class.getName() + " e where e.id=?").setParameter(0, id).executeUpdate();
    }

    @Override
    public void delete(DB dB) {
        Assert.notNull(dB, "要删除的对象不能为空!");
        getSession().delete(dB);
    }

    @Override
    public DB findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (DB) getSession().get(DB.class, id);
    }


    private void initCriteria(Criteria criteria, DBBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        if (bo == null) {
            return;
        }
        String type = bo.getType();
        if (StringUtils.equals(type, "5")) {
            bo.setType(null);
            criteria.add(Restrictions.in("type", new String[]{"1", "2"}));
        }
        if (StringUtils.equals(type, "6")) {
            bo.setType(null);
            criteria.add(Restrictions.in("type", new String[]{"3", "4"}));
        }
        CriteriaUtils.addCondition(criteria, bo);
    }

}