package com.michael.stock.stock.dao.impl;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.stock.stock.bo.StockDayBo;
import com.michael.stock.stock.dao.StockDayDao;
import com.michael.stock.stock.domain.StockDay;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("stockDayDao")
public class StockDayDaoImpl extends HibernateDaoHelper implements StockDayDao {

    @Override
    public String save(StockDay stockDay) {
        return (String) getSession().save(stockDay);
    }

    @Override
    public void update(StockDay stockDay) {
        getSession().update(stockDay);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<StockDay> query(StockDayBo bo) {
        Criteria criteria = createCriteria(StockDay.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<StockDay> pageQuery(StockDayBo bo) {
        Criteria criteria = createPagerCriteria(StockDay.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.asc("code"));
        return criteria.list();
    }

    @Override
    public Long getTotal(StockDayBo bo) {
        Criteria criteria = createRowCountsCriteria(StockDay.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + StockDay.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(StockDay stockDay) {
        Assert.notNull(stockDay, "要删除的对象不能为空!");
        getSession().delete(stockDay);
    }

    @Override
    public StockDay findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (StockDay) getSession().get(StockDay.class, id);
    }


    private void initCriteria(Criteria criteria, StockDayBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
        criteria.add(Restrictions.ge("seq", 6));
    }

}