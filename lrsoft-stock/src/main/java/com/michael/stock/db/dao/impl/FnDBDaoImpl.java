package com.michael.stock.db.dao.impl;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.stock.db.bo.FnDBBo;
import com.michael.stock.db.dao.FnDBDao;
import com.michael.stock.db.domain.FnDB;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("fnDBDao")
public class FnDBDaoImpl extends HibernateDaoHelper implements FnDBDao {

    @Override
    public String save(FnDB fnDB) {
        return (String) getSession().save(fnDB);
    }

    @Override
    public void update(FnDB fnDB) {
        getSession().update(fnDB);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FnDB> query(FnDBBo bo) {
        Criteria criteria = createCriteria(FnDB.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FnDB> pageQuery(FnDBBo bo) {
        Criteria criteria = createPagerCriteria(FnDB.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(FnDBBo bo) {
        Criteria criteria = createRowCountsCriteria(FnDB.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + FnDB.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(FnDB fnDB) {
        Assert.notNull(fnDB, "要删除的对象不能为空!");
        getSession().delete(fnDB);
    }

    @Override
    public FnDB findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (FnDB) getSession().get(FnDB.class, id);
    }


    private void initCriteria(Criteria criteria, FnDBBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}