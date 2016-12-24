package com.michael.stock.fn.service.impl;

import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.stock.db.domain.DB;
import com.michael.stock.fn.bo.Fn5Bo;
import com.michael.stock.fn.dao.Fn5Dao;
import com.michael.stock.fn.domain.Fn5;
import com.michael.stock.fn.service.Fn5Service;
import com.michael.stock.fn.service.Handle;
import com.michael.stock.fn.vo.Fn5Vo;
import com.michael.utils.date.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Michael
 */
@Service("fn5Service")
public class Fn5ServiceImpl implements Fn5Service, BeanWrapCallback<Fn5, Fn5Vo> {
    @Resource
    private Fn5Dao fn5Dao;

    private Map<Integer, Handle> handleMap = new HashMap<>();

    @Override
    public String save(Fn5 fn5) {
        validate(fn5);
        String id = fn5Dao.save(fn5);
        return id;
    }

    @Override
    public void update(Fn5 fn5) {
        validate(fn5);
        fn5Dao.update(fn5);
    }

    private void validate(Fn5 fn5) {
        ValidatorUtils.validate(fn5);
    }

    @Override
    public PageVo pageQuery(Fn5Bo bo) {
        PageVo vo = new PageVo();
        Long total = fn5Dao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Fn5> fn5List = fn5Dao.pageQuery(bo);
        List<Fn5Vo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(fn5List, Fn5Vo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public Fn5Vo findById(String id) {
        Fn5 fn5 = fn5Dao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(fn5, Fn5Vo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            fn5Dao.deleteById(id);
        }
    }

    @Override
    public List<Fn5Vo> query(Fn5Bo bo) {
        List<Fn5> fn5List = fn5Dao.query(bo);
        List<Fn5Vo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(fn5List, Fn5Vo.class);
        return vos;
    }


    @Override
    public void reset(Fn5Bo bo) {
        int fn = 10;    // 系数范围
        long f = 1000L * 60 * 60 * 24;
        Logger logger = Logger.getLogger(Fn5ServiceImpl.class);

        Integer type = bo.getType();
        Assert.notNull(type, "操作失败!类型不能为空!");
        Date date1 = bo.getDate1();
        Assert.notNull(date1, "操作失败!日期范围不能为空!");
        Date date2 = bo.getDate2();
        Assert.notNull(date2, "操作失败!日期范围不能为空!");

        logger.info(" *****************   RESET Fn5 : Start ************************** ");
        // 加载所有的日期
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
            logger.info(" *****************   RESET Fn5 : " + i + " ************************** ");

            // 删除原有数据
            session.createQuery("delete from " + Fn5.class.getName() + " f where f.type=?").setParameter(0, i).executeUpdate();
            List<Date> dates = session
                    .createQuery("select d.dbDate from " + DB.class.getName() + " d where d.type=? and d.dbDate>? and d.dbDate<? order by d.dbDate asc")
                    .setParameter(0, i + "")
                    .setParameter(1, bo.getDate1())
                    .setParameter(2, bo.getDate2())
                    .list();
            final int size = dates.size();
            Assert.isTrue(size < 11, "处理失败!数据量过大,最多只能匹配10个日期库中的日期，当前匹配[" + size + "]个!请缩小日期范围!");
            Assert.isTrue(size > 0, "处理失败!没有查询到符合条件的数据库日期，请扩大范围!");

            // 设定最大时间为2050年1月1日
            long maxDate = DateUtils.getDate(2050, 0, 1).getTime();
            long minDate = DateUtils.getDate(1990, 0, 1).getTime();

            for (int f1 = 0; f1 < size; f1++) {   // 第一层游标
                logger.info(String.format(" *****************   RESET Fn5 : %d (%d/%d) ************************** ", i, f1 + 1, size));
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
                            for (int f5 = size - 1; f5 > -1; f5--) {
                                if (f1 == f5 || f2 == f5 || f3 == f5 || f4 == f5) {
                                    continue;
                                }
                                Date d5 = dates.get(f5);
                                long date = d1.getTime() + d2.getTime() + d3.getTime() - d4.getTime() - d5.getTime();
                                if (date > maxDate) {
                                    break;
                                }
                                if (date < minDate) {
                                    continue;
                                }
                                for (int x = -fn; x <= fn; x++) {
                                    Date bk = new Date(date + f * x);
                                    Fn5 fn5 = new Fn5();
                                    fn5.setA1(d1);
                                    fn5.setA2(d2);
                                    fn5.setA3(d3);
                                    fn5.setA4(d4);
                                    fn5.setA5(d5);
                                    fn5.setBk(bk);
                                    fn5.setType(i);
                                    fn5.setFn(x);
                                    total++;
                                    session.save(fn5);
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
        }
        logger.info(" *****************   RESET Fn5 : End，共保存" + total + "条数据 ************************** ");
        handleMap.put(type, new Handle(new Date(), date1, date2));
    }


    @Override
    public Handle lastHandle(Integer type) {
        return handleMap.get(type);
    }

    @Override
    public void doCallback(Fn5 fn5, Fn5Vo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

    }
}
