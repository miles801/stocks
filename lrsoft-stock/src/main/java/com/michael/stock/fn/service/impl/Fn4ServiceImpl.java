package com.michael.stock.fn.service.impl;

import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.core.pager.Pager;
import com.michael.stock.db.domain.DB;
import com.michael.stock.fn.bo.Fn4Bo;
import com.michael.stock.fn.dao.Fn4Dao;
import com.michael.stock.fn.domain.Fn4;
import com.michael.stock.fn.service.Fn4Service;
import com.michael.stock.fn.service.Handle;
import com.michael.stock.fn.vo.Fn4Vo;
import com.michael.utils.date.DateUtils;
import com.michael.utils.number.IntegerUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Michael
 */
@Service("fn4Service")
public class Fn4ServiceImpl implements Fn4Service, BeanWrapCallback<Fn4, Fn4Vo> {
    @Resource
    private Fn4Dao fn4Dao;

    private Map<Integer, Handle> handleMap = new HashMap<>();

    @Override
    public String save(Fn4 fn4) {
        validate(fn4);
        String id = fn4Dao.save(fn4);
        return id;
    }

    @Override
    public void update(Fn4 fn4) {
        validate(fn4);
        fn4Dao.update(fn4);
    }

    private void validate(Fn4 fn4) {
        ValidatorUtils.validate(fn4);
    }

    @Override
    public PageVo pageQuery(Fn4Bo bo) {
        PageVo vo = new PageVo();
        Session session = HibernateUtils.getSession(false);
        Criteria criteria = session.createCriteria(Fn4.class);
        criteria.setProjection(Projections.countDistinct("bk"));
        CriteriaUtils.addCondition(criteria, bo);
        Long total = (Long) criteria.uniqueResult();
        vo.setTotal(total);
        criteria = session.createCriteria(Fn4.class);
        criteria.setProjection(Projections.projectionList()
                .add(Projections.count("bk").as("bkCount"))
                .add(Projections.groupProperty("bk").as("bk"))
        );

        CriteriaUtils.addCondition(criteria, bo);
        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        criteria.addOrder(Order.desc("bkCount"));
        criteria.addOrder(Order.asc("bk"));
        criteria.setFirstResult(IntegerUtils.add(Pager.getStart()));
        criteria.setMaxResults(IntegerUtils.add(Pager.getLimit()));
        List<Map<String, Object>> data = criteria.list();

        for (Map<String, Object> o : data) {
            Date bk = (Date) o.get("bk");
            bo.setBk(bk);
            o.put("data", fn4Dao.query(bo));
        }
        vo.setData(data);
        return vo;
    }


    @Override
    public Fn4Vo findById(String id) {
        Fn4 fn4 = fn4Dao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(fn4, Fn4Vo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            fn4Dao.deleteById(id);
        }
    }

    @Override
    public List<Fn4Vo> query(Fn4Bo bo) {
        List<Fn4> fn4List = fn4Dao.query(bo);
        List<Fn4Vo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(fn4List, Fn4Vo.class);
        return vos;
    }

    @Override
    public Handle lastHandle(Integer type) {
        return handleMap.get(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reset(Fn4Bo bo) {
        int fn = 10;    // 系数范围
        long f = 1000L * 60 * 60 * 24;
        Logger logger = Logger.getLogger(Fn4ServiceImpl.class);
        Integer type = bo.getType();
        Assert.notNull(type, "操作失败!类型不能为空!");
        Date date1 = bo.getDate1();
        Assert.notNull(date1, "操作失败!日期范围不能为空!");
        Date date2 = bo.getDate2();
        Assert.notNull(date2, "操作失败!日期范围不能为空!");

        logger.info(" *****************   RESET Fn4 : Start ************************** ");
        final Session session = HibernateUtils.getSession(false);

        List<Integer> types = new ArrayList<>();
        if (type == 5) {
            types.add(1);
            types.add(2);
        } else if (type == 6) {
            types.add(3);
            types.add(4);
        } else {
            types.add(type);
        }
        int total = 0;
        for (Integer i : types) {
            // 删除原有数据
            session.createQuery("delete from " + Fn4.class.getName() + " f where f.type=?").setParameter(0, i).executeUpdate();
            List<Date> dates = session
                    .createQuery("select d.dbDate from " + DB.class.getName() + " d where d.type=? and d.dbDate>? and d.dbDate<? order by d.dbDate asc")
                    .setParameter(0, i + "")
                    .setParameter(1, bo.getDate1())
                    .setParameter(2, bo.getDate2())
                    .list();
            final int size = dates.size();
            Assert.isTrue(size < 16, "处理失败!数据量过大,最多只能匹配10个日期库中的日期，当前匹配[" + size + "]个!请缩小日期范围!");
            Assert.isTrue(size > 0, "查询失败!没有查询到符合条件的数据库日期，请扩大范围!");

            // 设定最大时间为2050年1月1日
            long maxDate = DateUtils.getDate(2050, 0, 1).getTime();
            long minDate = DateUtils.getDate(1990, 0, 1).getTime();

            for (int f1 = 0; f1 < size; f1++) {   // 第一层游标
                logger.info(String.format(" *****************   RESET Fn4 : %d (%d/%d) ************************** ", i, f1 + 1, size));
                Date d1 = dates.get(f1);
                for (int f2 = 0; f2 < size; f2++) {
                    Date d2 = dates.get(f2);
                    if (f1 == f2) {
                        continue;
                    }
                    for (int f3 = 0; f3 < size; f3++) {
                        Date d3 = dates.get(f3);
                        if (f1 == f3 || f2 == f3) {
                            continue;
                        }
                        for (int f4 = size - 1; f4 > -1; f4--) {
                            if (f1 == f4 || f2 == f4 || f3 == f4) {
                                continue;
                            }
                            Date d4 = dates.get(f4);
                            long date = d1.getTime() + d2.getTime() + d3.getTime() - d4.getTime();
                            if (date > maxDate) {
                                break;
                            }
                            if (date < minDate) {
                                continue;
                            }
                            for (int x = -fn; x <= fn; x++) {
                                Date bk = new Date(date + f * x);
                                Fn4 fn4 = new Fn4();
                                fn4.setA1(d1);
                                fn4.setA2(d2);
                                fn4.setA3(d3);
                                fn4.setA4(d4);
                                fn4.setBk(bk);
                                fn4.setType(i);
                                fn4.setFn(x);
                                total++;
                                session.save(fn4);
                            }
                            if (total % 20 == 0) {
                                session.flush();
                                session.clear();
                            }
                        }
                    }
                }
            }
        }
        logger.info(" *****************   RESET Fn4 : End，共保存" + total + "条数据 ************************** ");
        handleMap.put(type, new Handle(new Date(), date1, date2));
    }

    @Override
    public void doCallback(Fn4 fn4, Fn4Vo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

    }
}
