package com.michael.stock.fn.service.impl;

import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.Order;
import com.michael.core.pager.PageVo;
import com.michael.core.pager.Pager;
import com.michael.stock.db.domain.DB;
import com.michael.stock.fn.bo.Fn3Bo;
import com.michael.stock.fn.dao.Fn3Dao;
import com.michael.stock.fn.domain.Fn3;
import com.michael.stock.fn.service.Fn3Service;
import com.michael.stock.fn.vo.Fn3Vo;
import com.michael.utils.date.DateUtils;
import com.michael.utils.number.IntegerUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Michael
 */
@Service("fn3Service")
public class Fn3ServiceImpl implements Fn3Service, BeanWrapCallback<Fn3, Fn3Vo> {
    @Resource
    private Fn3Dao fn3Dao;

    @Override
    public String save(Fn3 fn3) {
        validate(fn3);
        String id = fn3Dao.save(fn3);
        return id;
    }

    @Override
    public void update(Fn3 fn3) {
        validate(fn3);
        fn3Dao.update(fn3);
    }

    private void validate(Fn3 fn3) {
        ValidatorUtils.validate(fn3);
    }

    @Override
    public PageVo pageQuery(Fn3Bo bo) {
        PageVo vo = new PageVo();
        Session session = HibernateUtils.getSession(false);
        Criteria criteria = session.createCriteria(Fn3.class);
        criteria.setProjection(Projections.countDistinct("bk"));
        CriteriaUtils.addCondition(criteria, bo);
        Long total = (Long) criteria.uniqueResult();
        vo.setTotal(total);
        criteria = session.createCriteria(Fn3.class);
        criteria.setProjection(Projections.projectionList()
                .add(Projections.count("bk").as("bkCount"))
                .add(Projections.groupProperty("bk").as("bk"))
        );

        CriteriaUtils.addCondition(criteria, bo);
        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order order = Pager.getOrder().next();
            String propertyName = order.getName();
            criteria.addOrder(order.isReverse() ? org.hibernate.criterion.Order.desc(propertyName) : org.hibernate.criterion.Order.asc(propertyName));
        } else {
            criteria.addOrder(org.hibernate.criterion.Order.desc("bkCount"));
            criteria.addOrder(org.hibernate.criterion.Order.asc("bk"));
        }
        criteria.setFirstResult(IntegerUtils.add(Pager.getStart()));
        criteria.setMaxResults(IntegerUtils.add(Pager.getLimit()));
        List<Map<String, Object>> data = criteria.list();

        for (Map<String, Object> o : data) {
            Date bk = (Date) o.get("bk");
            bo.setBk(bk);
            o.put("data", fn3Dao.query(bo));
        }
        vo.setData(data);
        return vo;
    }


    @Override
    public Fn3Vo findById(String id) {
        Fn3 fn3 = fn3Dao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(fn3, Fn3Vo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            fn3Dao.deleteById(id);
        }
    }

    @Override
    public List<Fn3Vo> query(Fn3Bo bo) {
        List<Fn3> fn3List = fn3Dao.query(bo);
        List<Fn3Vo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(fn3List, Fn3Vo.class);
        return vos;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reset() {
        int fn = 10;    // 系数范围
        long f = 1000L * 60 * 60 * 24;
        final Session session = HibernateUtils.getSession(false);
        Logger logger = Logger.getLogger(Fn3ServiceImpl.class);
        logger.info(" *****************   RESET Fn3 : Start ************************** ");
        // 加载所有的日期
        // 删除原有数据
        session.createSQLQuery("TRUNCATE table stock_fn3").executeUpdate();
        // 设定最大时间为2050年1月1日
        long maxDate = DateUtils.getDate(2050, 0, 1).getTime();
        long minDate = DateUtils.getDate(1990, 0, 1).getTime();
        // 加载所有的日期
        int total = 0;
        for (int i = 1; i < 5; i++) {
            logger.info(" *****************   RESET Fn3 : " + i + " ************************** ");
            List<Date> dates = session
                    .createQuery("select d.dbDate from " + DB.class.getName() + " d where d.type=? order by d.dbDate asc")
                    .setParameter(0, i + "")
                    .list();
            if (dates.isEmpty()) {
                continue;
            }
            int size = dates.size();
            for (int f1 = 0; f1 < size; f1++) {   // 第一层游标
                logger.info(String.format(" *****************   RESET Fn3 : %d (%d/%d) ************************** ", i, f1 + 1, size));
                long d1 = dates.get(f1).getTime();
                for (int f2 = 0; f2 < size; f2++) {
                    if (f1 == f2) {
                        continue;
                    }
                    long d2 = dates.get(f2).getTime();
                    for (int f3 = size - 1; f3 > -1; f3--) {
                        if (f1 == f3 || f2 == f3) {
                            continue;
                        }
                        long d3 = dates.get(f3).getTime();
                        long date = d1 + d2 - d3;
                        if (date > maxDate) {
                            break;
                        }
                        if (date < minDate) {
                            continue;
                        }
                        for (int x = -fn; x <= fn; x++) {
                            Date bk = new Date(date + f * x);
                            Fn3 fn3 = new Fn3();
                            fn3.setA1(new Date(d1));
                            fn3.setA2(new Date(d2));
                            fn3.setA3(new Date(d3));
                            fn3.setBk(bk);
                            fn3.setType(i);
                            fn3.setFn(x);
                            total++;
                            session.save(fn3);
                        }
                        if (total % 20 == 0) {
                            session.flush();
                            session.clear();
                        }
                    }
                }
            }
        }
        logger.info(" *****************   RESET Fn3 : End，共保存" + total + "条数据 ************************** ");
    }

    @Override
    public void doCallback(Fn3 fn3, Fn3Vo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

    }
}
