package com.michael.stock.stock.dao.impl;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.stock.stock.bo.StockWeekBo;
import com.michael.stock.stock.dao.StockWeekDao;
import com.michael.stock.stock.domain.StockWeek;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("stockWeekDao")
public class StockWeekDaoImpl extends HibernateDaoHelper implements StockWeekDao {

    @Override
    public String save(StockWeek stockWeek) {
        return (String) getSession().save(stockWeek);
    }

    @Override
    public void update(StockWeek stockWeek) {
        getSession().update(stockWeek);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<StockWeek> query(StockWeekBo bo) {
        Criteria criteria = createCriteria(StockWeek.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<StockWeek> pageQuery(StockWeekBo bo) {
        Criteria criteria = createPagerCriteria(StockWeek.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(StockWeekBo bo) {
        Criteria criteria = createRowCountsCriteria(StockWeek.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + StockWeek.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(StockWeek stockWeek) {
        Assert.notNull(stockWeek, "要删除的对象不能为空!");
        getSession().delete(stockWeek);
    }

    @Override
    public StockWeek findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (StockWeek) getSession().get(StockWeek.class, id);
    }


    private void initCriteria(Criteria criteria, StockWeekBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
        criteria.add(Restrictions.ge("seq", 6));
    }

}