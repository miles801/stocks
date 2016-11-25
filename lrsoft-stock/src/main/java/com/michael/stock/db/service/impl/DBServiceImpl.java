package com.michael.stock.db.service.impl;

import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.stock.db.bo.DBBo;
import com.michael.stock.db.dao.DBDao;
import com.michael.stock.db.dao.FnDBDao;
import com.michael.stock.db.domain.DB;
import com.michael.stock.db.service.DBService;
import com.michael.stock.db.service.FnDBService;
import com.michael.stock.db.vo.DBVo;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.michael.core.hibernate.HibernateUtils.getSession;

/**
 * @author Michael
 */
@Service("dBService")
public class DBServiceImpl implements DBService, BeanWrapCallback<DB, DBVo> {
    @Resource
    private DBDao dBDao;

    @Resource
    private FnDBDao fnDBDao;

    @Resource
    private FnDBService fnDBService;

    @Override
    public String save(DB dB) {
        validate(dB);
        String id = dBDao.save(dB);

        // 计算某个日期的fn数据库
        fnDBService.add(dB.getType(), dB.getDbDate());

        return id;
    }

    @Override
    public void update(DB dB) {
        validate(dB);
        Date originDate = (Date) getSession(false)
                .createQuery("select d.dbDate from " + DB.class.getName() + " d where d.id=?")
                .setParameter(0, dB.getId())
                .uniqueResult();
        if (originDate.getTime() != dB.getDbDate().getTime()) {
            fnDBService.delete(dB.getType(), originDate);
            fnDBService.add(dB.getType(), dB.getDbDate());
        }
        dBDao.update(dB);
    }

    private void validate(DB dB) {
        ValidatorUtils.validate(dB);
    }

    @Override
    public PageVo pageQuery(DBBo bo) {
        PageVo vo = new PageVo();
        Long total = dBDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<DB> dBList = dBDao.pageQuery(bo);
        List<DBVo> vos = BeanWrapBuilder.newInstance().setCallback(this).wrapList(dBList, DBVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public DBVo findById(String id) {
        DB dB = dBDao.findById(id);
        return BeanWrapBuilder.newInstance().wrap(dB, DBVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            DB db = dBDao.findById(id);
            if (db != null) {
                fnDBService.delete(db.getType(), db.getDbDate());
                dBDao.delete(db);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> calculate(int type, String db, int value) {
        // 加载所有的日期
        Criteria criteria = HibernateUtils.getSession(false)
                .createCriteria(DB.class)
                .setProjection(Projections.distinct(Projections.property("dbDate")));
        if (StringUtils.include(db, "1", "2", "3", "4")) {
            criteria.add(Restrictions.eq("type", db));
        }
        if (StringUtils.equals(db, "5")) {
            criteria.add(Restrictions.in("type", new String[]{"1", "2"}));
        }
        if (StringUtils.equals(db, "6")) {
            criteria.add(Restrictions.in("type", new String[]{"3", "4"}));
        }
        criteria.addOrder(Order.asc("dbDate"));
        List<Date> dates = criteria.list();
        List<Map<String, Object>> data = new ArrayList<>();
        final int size = dates.size();
        if (size < type + 1) {
            return data;
        }
        long days = value * 1000L * 60 * 60 * 24;
        if (type == 3) {
            int f1 = 0, f2 = 1, f3 = 2, f4 = 3;
            for (; f1 < size - 3; f1++) {   // 第一层游标
                long d1 = dates.get(f1).getTime();
                for (f2 = f1 + 1; f2 < size - 2; f2++) {
                    long d2 = dates.get(f2).getTime();
                    for (f3 = f2 + 1; f3 < size - 1; f3++) {
                        long d3 = dates.get(f3).getTime();
                        for (f4 = f3 + 1; f4 < size; f4++) {
                            long d4 = dates.get(f4).getTime();
                            if ((d2 + d3 - d4 - days <= d1) && d2 + d3 - d4 + days >= d1) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("bk", d1);
                                map.put("a1", d2);
                                map.put("a2", d3);
                                map.put("a3", d4);
                                data.add(map);
                                break;
                            }
                            if (d1 > d2 + d3 - d4 + days) {
                                break;
                            }
                        }
                    }
                }
            }
        } else if (type == 4) {
            int f1 = 0, f2 = 1, f3 = 2, f4 = 3, f5 = 4;
            for (; f1 < size - 4; f1++) {   // 第一层游标
                long d1 = dates.get(f1).getTime();
                for (f2 = f1 + 1; f2 < size - 3; f2++) {
                    long d2 = dates.get(f2).getTime();
                    for (f3 = f2 + 1; f3 < size - 2; f3++) {
                        long d3 = dates.get(f3).getTime();
                        for (f4 = f3 + 1; f4 < size - 1; f4++) {
                            long d4 = dates.get(f4).getTime();
                            for (f5 = f4 + 1; f5 < size; f5++) {
                                long d5 = dates.get(f5).getTime();
                                if ((d2 + d3 + d4 - d5 - days <= d1) && d2 + d3 + d4 - d5 + days >= d1) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("bk", d1);
                                    map.put("a1", d2);
                                    map.put("a2", d3);
                                    map.put("a3", d4);
                                    map.put("a4", d5);
                                    data.add(map);
                                    break;
                                }
                                if (d1 > d2 + d3 + d4 - d5 + days) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return data;
    }

    @Override
    public List<DBVo> query(DBBo bo) {
        List<DB> dBList = dBDao.query(bo);
        List<DBVo> vos = BeanWrapBuilder.newInstance().setCallback(this).wrapList(dBList, DBVo.class);
        return vos;
    }


    @Override
    public void doCallback(DB dB, DBVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

        // 类型
        vo.setTypeName(container.getSystemName("DB_TYPE", dB.getType()));

    }
}
