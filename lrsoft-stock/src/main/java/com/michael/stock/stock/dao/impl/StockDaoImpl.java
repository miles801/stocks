package com.michael.stock.stock.dao.impl;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.stock.stock.bo.StockBo;
import com.michael.stock.stock.dao.StockDao;
import com.michael.stock.stock.domain.Stock;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("stockDao")
public class StockDaoImpl extends HibernateDaoHelper implements StockDao {

    @Override
    public String save(Stock stock) {
        return (String) getSession().save(stock);
    }

    @Override
    public void update(Stock stock) {
        getSession().update(stock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Stock> query(StockBo bo) {
        Criteria criteria = createCriteria(Stock.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Stock> pageQuery(StockBo bo) {
        Criteria criteria = createPagerCriteria(Stock.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(StockBo bo) {
        Criteria criteria = createRowCountsCriteria(Stock.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Stock.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Stock stock) {
        Assert.notNull(stock, "要删除的对象不能为空!");
        getSession().delete(stock);
    }

    @Override
    public Stock findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Stock) getSession().get(Stock.class, id);
    }

    @Override
    public boolean hasCode(String code, String id) {
        Assert.hasText(code, "查询失败!编号不能为空!");
        Criteria criteria = createRowCountsCriteria(Stock.class)
                .add(Restrictions.eq("code", code));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        return (Long) criteria.uniqueResult() > 0;
    }

    @Override
    public boolean hasName(String name, String id) {
        Assert.hasText(name, "查询失败!名称不能为空!");
        Criteria criteria = createRowCountsCriteria(Stock.class)
                .add(Restrictions.eq("name", name));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        return (Long) criteria.uniqueResult() > 0;
    }

    @Override
    public String maxCode(int type) {
        return (String) createCriteria(Stock.class)
                .setProjection(Projections.max("code"))
                .add(Restrictions.eq("type", type))
                .uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> queryCode() {
        return (List<String>) createCriteria(Stock.class)
                .setProjection(Projections.property("code"))
                .list();
    }

    private void initCriteria(Criteria criteria, StockBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}