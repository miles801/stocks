package com.michael.stock.stock.service.impl;

import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.Order;
import com.michael.core.pager.PageVo;
import com.michael.core.pager.Pager;
import com.michael.stock.stock.bo.StockWeekBo;
import com.michael.stock.stock.dao.StockWeekDao;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.domain.StockWeek;
import com.michael.stock.stock.service.StockWeekService;
import com.michael.stock.stock.vo.StockWeekVo;
import com.michael.utils.collection.CollectionUtils;
import com.michael.utils.date.DateUtils;
import com.michael.utils.number.DoubleUtils;
import com.michael.utils.number.IntegerUtils;
import com.michael.utils.string.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * @author Michael
 */
@Service("stockWeekService")
public class StockWeekServiceImpl implements StockWeekService, BeanWrapCallback<StockWeek, StockWeekVo> {
    @Resource
    private StockWeekDao stockWeekDao;

    @Override
    public String save(StockWeek stockWeek) {
        validate(stockWeek);
        String id = stockWeekDao.save(stockWeek);
        return id;
    }

    @Override
    public void update(StockWeek stockWeek) {
        validate(stockWeek);
        stockWeekDao.update(stockWeek);
    }

    private void validate(StockWeek stockWeek) {
        ValidatorUtils.validate(stockWeek);
    }

    @Override
    public PageVo pageQuery(StockWeekBo bo) {
        PageVo vo = new PageVo();
        Long total = stockWeekDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<StockWeek> stockWeekList = stockWeekDao.pageQuery(bo);
        List<StockWeekVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(stockWeekList, StockWeekVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public StockWeekVo findById(String id) {
        StockWeek stockWeek = stockWeekDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(stockWeek, StockWeekVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            stockWeekDao.deleteById(id);
        }
    }

    @Override
    public List<StockWeekVo> query(StockWeekBo bo) {
        List<StockWeek> stockWeekList = stockWeekDao.query(bo);
        List<StockWeekVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(stockWeekList, StockWeekVo.class);
        return vos;
    }


    public void importData(String[] attachmentIds) {
        Assert.isTrue(false, "该方法不可用!");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reset(String stockCode) {
        Assert.hasText(stockCode, "操作失败!股票代码不能为空!");

        // 删除当前股票的周K数据
        Session session = HibernateUtils.getSession(false);
        session.createQuery("delete from " + StockWeek.class.getName() + " s where s.code=?")
                .setParameter(0, stockCode)
                .executeUpdate();

        Date startDate = DateUtils.getDate(1970, 1, 1);  // 相当于从最开始开始
        Date today = DateUtils.getDayBegin(new Date());
        int index = 0;
        // 最近5周的数组
        List<StockWeek> weeks = new ArrayList<>();

        // 取出最早的5只股票
        List<StockDay> stockDays = session.createQuery("from " + StockDay.class.getName() + " s where s.businessDate>? and  s.code=? order by s.businessDate asc")
                .setParameter(0, startDate)
                .setParameter(1, stockCode)
                .setMaxResults(50)
                .list();
        while (true) {
            int size = stockDays.size();
            if (size < 5) {
                stockDays.addAll(session.createQuery("from " + StockDay.class.getName() + " s where s.businessDate>? and  s.code=? order by s.businessDate asc")
                        .setParameter(0, startDate)
                        .setParameter(1, stockCode)
                        .setFirstResult(size)
                        .setMaxResults(50)
                        .list());
            }
            if (CollectionUtils.isEmpty(stockDays)) {
                break;  // 如果已经没有股票了则退出循环
            }
            // 如果最后一个日期的周数和今天的年周数一致,且今天不是周五，则表明读取到了最后
            if (stockDays.size() < 5) {
                if (DateUtils.getYearWeek(startDate) == DateUtils.getWeek(today) && today.getYear() == startDate.getYear() && DateUtils.getWeek(today) != Calendar.FRIDAY) {
                    break;
                }
            }

            // 设置周K数据并保存
            Date closeDate = initAndSave(session, weeks, stockDays);
            index++;
            if (index % 10 == 0) {
                session.flush();
                session.clear();
            }

            startDate = closeDate;   // 重置
        }
    }

    private Date initAndSave(Session session, List<StockWeek> weeks, List<StockDay> stockDays) {
        String stockCode = stockDays.get(0).getCode();
        // 获取本周的基本数据
        double highPrice = 0d;   // 最高价
        double lowPrice = 0d;    // 最低价
        double openPrice = 0d;     // 开盘价
        double closePrice = 0d;     // 开盘价
        Date openDate = null;     // 开盘日期
        Date closeDate = null;    // 收盘日期
        int openTimes = 0; // 这一周的开盘天数
        int weekNo = 0;
        final int size = weeks.size();
        List<StockDay> thisWeekDays = new ArrayList<>();
        for (StockDay day : stockDays) {
            Date businessDate = day.getBusinessDate();
            int nowWeekDay = DateUtils.getYearWeek(businessDate);
            if (weekNo == 0) {
                weekNo = nowWeekDay;
            }
            if (weekNo != nowWeekDay) {  // 如果取出的周数不在同一周，则视为下一周的数据
                break;
            }
            thisWeekDays.add(day);
            weekNo = nowWeekDay;
            if (openDate == null) {
                openDate = day.getBusinessDate();
                openPrice = day.getOpenPrice();
                lowPrice = day.getLowPrice();
            }
            closeDate = day.getBusinessDate();
            closePrice = day.getClosePrice();
            openTimes++;
            highPrice = day.getHighPrice() > highPrice ? day.getHighPrice() : highPrice;
            lowPrice = day.getLowPrice() < lowPrice ? day.getLowPrice() : lowPrice;
        }

        // 创建周数据
        StockWeek stockWeek = new StockWeek();
        stockWeek.setOpenPrice(openPrice);
        stockWeek.setOpenDate(openDate);
        stockWeek.setClosePrice(closePrice);
        stockWeek.setCloseDate(closeDate);
        stockWeek.setOpenTimes(openTimes);
        stockWeek.setHighPrice(highPrice);
        stockWeek.setLowPrice(lowPrice);
        stockWeek.setCode(stockCode);
        stockWeek.setName(stockDays.get(0).getName());
        stockWeek.setUpdown(closePrice - openPrice);
        stockWeek.setKey((stockWeek.getUpdown() > 0 ? "000001" : "000000"));
        stockWeek.setKey3((stockWeek.getUpdown() > 0 ? "001" : "000"));
        if (size > 0) {
            final StockWeek lastWeek = weeks.get(size - 1);
            stockWeek.setSeq(IntegerUtils.add(lastWeek.getSeq(), 1));
            stockWeek.setYesterdayClosePrice(lastWeek.getClosePrice());
            stockWeek.setKey3(lastWeek.getKey3().substring(1) + (stockWeek.getUpdown() > 0 ? "1" : "0"));
            stockWeek.setKey(lastWeek.getKey().substring(1) + (stockWeek.getUpdown() > 0 ? "1" : "0"));

            // 设置3线数据
            int index = size - 4;
            if (size > 4) {
                stockWeek.setDate3(weeks.get(index + 1).getOpenDate());
                stockWeek.setD1((weeks.get(index).getClosePrice() - weeks.get(index).getYesterdayClosePrice()) / weeks.get(index).getYesterdayClosePrice());
                stockWeek.setD2((weeks.get(index + 1).getClosePrice() - weeks.get(index + 1).getYesterdayClosePrice()) / weeks.get(index + 1).getYesterdayClosePrice());
                stockWeek.setD3((weeks.get(index + 2).getClosePrice() - weeks.get(index + 2).getYesterdayClosePrice()) / weeks.get(index + 2).getYesterdayClosePrice());
                // 四日、七日的高低
                lastWeek.setNextHigh((stockWeek.getHighPrice() - lastWeek.getClosePrice()) / lastWeek.getClosePrice());
                lastWeek.setNextLow((stockWeek.getLowPrice() - lastWeek.getClosePrice()) / lastWeek.getClosePrice());
                // 四七日阴阳
                if (stockWeek.getClosePrice() - stockWeek.getOpenPrice() == 0) {
                    lastWeek.setYang(stockWeek.getClosePrice() > DoubleUtils.add(stockWeek.getYesterdayClosePrice()));
                } else {
                    lastWeek.setYang(stockWeek.getClosePrice() > stockWeek.getOpenPrice());
                }
            }

            // 设置6线数据
            if (size == 8) {
                stockWeek.setDate6(weeks.get(1).getOpenDate());
                stockWeek.setP1((weeks.get(1).getClosePrice() - weeks.get(1).getYesterdayClosePrice()) / weeks.get(1).getYesterdayClosePrice());
                stockWeek.setP2((weeks.get(2).getClosePrice() - weeks.get(2).getYesterdayClosePrice()) / weeks.get(2).getYesterdayClosePrice());
                stockWeek.setP3((weeks.get(3).getClosePrice() - weeks.get(3).getYesterdayClosePrice()) / weeks.get(3).getYesterdayClosePrice());
                stockWeek.setP4((weeks.get(4).getClosePrice() - weeks.get(4).getYesterdayClosePrice()) / weeks.get(4).getYesterdayClosePrice());
                stockWeek.setP5((weeks.get(5).getClosePrice() - weeks.get(5).getYesterdayClosePrice()) / weeks.get(5).getYesterdayClosePrice());
                stockWeek.setP6((weeks.get(6).getClosePrice() - weeks.get(6).getYesterdayClosePrice()) / weeks.get(6).getYesterdayClosePrice());
            }

            session.update(lastWeek);
        }


        String id = (String) session.save(stockWeek);
        stockWeek.setId(id);
        weeks.add(stockWeek);
        if (weeks.size() > 8) {
            weeks.remove(0);
        }
        stockDays.removeAll(thisWeekDays);  // 删除本周的数据
        return closeDate;
    }

    /**
     * 创建一个初始化的StockWeek对象
     */
    private StockWeek createStockWeek() {
        StockWeek week = new StockWeek();
        week.setOpenDate(null);
        week.setCloseDate(null);
        week.setOpenPrice(0d);
        week.setClosePrice(0d);
        week.setYesterdayClosePrice(0d);
        week.setDate3(null);
        week.setDate6(null);
        week.setHighPrice(0d);
        week.setLowPrice(0d);
        week.setKey3("000");
        week.setKey("000000");
        week.setNextLow(0d);
        week.setNextHigh(0d);
        week.setP1(0d);
        week.setP2(0d);
        week.setP3(0d);
        week.setP4(0d);
        week.setP5(0d);
        week.setUpdown(0d);
        week.setYang(false);
        return week;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(String stockCode) {
        Assert.hasText(stockCode, "操作失败!股票代码不能为空!");
        // 判断今天是不是周五/周六/周日，如果不是则直接返回
        Date today = DateUtils.getDayBegin(new Date());
        final int week = DateUtils.getWeek(today);
        if (week != Calendar.FRIDAY && week != Calendar.SUNDAY && week != Calendar.SATURDAY) {
            return;
        }
        // 获取周一的日期
        int days = 4;
        if (week == Calendar.SUNDAY) {
            days = 6;
        } else if (week == Calendar.SATURDAY) {
            days = 5;
        }
        Date monday = org.apache.commons.lang.time.DateUtils.addDays(today, -days);

        Session session = HibernateUtils.getSession(false);
        // 删除历史数据
        session.createQuery("delete from " + StockWeek.class.getName() + " s where s.openDate=? and s.code=?")
                .setParameter(0, monday)
                .setParameter(1, stockCode)
                .executeUpdate();

        // 获取周一到周五的交易数据
        List<StockDay> stockDays = session.createQuery("from " + StockDay.class.getName() + " s where s.businessDate >=? and s.code=? ORDER BY s.businessDate asc")
                .setParameter(0, monday)
                .setParameter(1, stockCode)
                .setMaxResults(5)
                .list();
        if (CollectionUtils.isEmpty(stockDays)) {
            return;
        }

        // 获取当前股票前5周的数据
        List<StockWeek> weeks = session.createQuery("from " + StockWeek.class.getName() + " s where s.code=? order by s.closeDate desc")
                .setParameter(0, stockCode)
                .setMaxResults(5)
                .list();

        // 设置并保存
        initAndSave(session, weeks, stockDays);
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> report3(StockWeekBo bo) {
        Session session = HibernateUtils.getSession(false);
        String sql = "select s_key3 as key1,code," +
                "sum(case isYang when 1 then 1 else 0 end) as yang," +
                "sum(nextHigh) as nextHigh,sum(nextLow) as nextLow,count(id) as counts " +
                "from stock_week where nextHigh is not null ";
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotEmpty(bo.getCode())) {
            sql += " and code=? ";
            params.add(bo.getCode());
        }
        if (StringUtils.isNotEmpty(bo.getKey3())) {
            sql += " and s_key3=? ";
            params.add(bo.getKey3());
        }
        sql += " group by s_key3,code ";
        sql = "select t.*,t.nextHigh/t.counts as avgHigh,t.nextLow/t.counts as avgLow ,t.yang/t.counts as per from (" + sql + ") t ";
        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order o = Pager.getOrder().next();
            sql += " order by " + o.getName() + (o.isReverse() ? " desc " : " asc ");
        } else {
            sql += " order by t.key1 asc ";
        }
        Query query = session.createSQLQuery(sql);
        int index = 0;
        for (Object o : params) {
            query.setParameter(index++, o);
        }
        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(IntegerUtils.add(Pager.getStart()))
                .setMaxResults(IntegerUtils.add(Pager.getLimit()))
                .list();
    }

    public PageVo result3(StockWeekBo bo) {
        PageVo vo = new PageVo();
        int start = IntegerUtils.add(Pager.getStart());
        int limit = IntegerUtils.add(Pager.getLimit());
        Session session = HibernateUtils.getSession(false);
        String coreSql = " from result_week3 rd" +
                " JOIN (select concat(d.code,':',d.s_key3) as name from stock_week d join (select max(closeDate) closeDate from stock_week) t on t.closeDate=d.closeDate where d.nextHigh is null ) t\n" +
                " on rd.name=t.name ";
        List<Object> params = new ArrayList<>();
        if (bo != null) {
            if (StringUtils.isNotEmpty(bo.getCode())) {
                coreSql += " and code=? ";
                params.add(bo.getCode());
            }
            if (StringUtils.isNotEmpty(bo.getKey3())) {
                coreSql += " and key1=? ";
                params.add(bo.getKey3());
            }
        }
        Query totalQuery = session.createSQLQuery("select count(rd.name) " + coreSql);
        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order o = Pager.getOrder().next();
            coreSql += " order by rd." + o.getName() + (o.isReverse() ? " desc " : " asc ");
        } else {
            coreSql += " order by rd.key1 asc ";
        }
        Query query = session.createSQLQuery("select rd.* " + coreSql);
        if (CollectionUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.size(); i++) {
                totalQuery.setParameter(i, params.get(i));
                query.setParameter(i, params.get(i));
            }
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        BigInteger bigInteger = (BigInteger) totalQuery.uniqueResult();
        if (bigInteger == null || bigInteger.intValue() == 0) {
            vo.setTotal(0L);
            return vo;
        }
        vo.setTotal(bigInteger.longValue());
        query.setFirstResult(start);
        query.setMaxResults(limit);
        List<Map<String, Object>> data = query.list();
        vo.setData(data);
        return vo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageVo result6(StockWeekBo bo) {
        PageVo vo = new PageVo();
        int start = IntegerUtils.add(Pager.getStart());
        int limit = IntegerUtils.add(Pager.getLimit());
        Session session = HibernateUtils.getSession(false);
        String coreSql = " from result_week6 rd " +
                " JOIN (select concat(d.code,':',d.s_key) as name from stock_week d join (select max(closeDate) closeDate from stock_week) t on t.closeDate=d.closeDate where d.nextHigh is null ) t " +
                " on rd.name=t.name ";
        List<Object> params = new ArrayList<>();
        if (bo != null) {
            if (StringUtils.isNotEmpty(bo.getCode())) {
                coreSql += " and code=? ";
                params.add(bo.getCode());
            }
            if (StringUtils.isNotEmpty(bo.getKey())) {
                coreSql += " and key1=? ";
                params.add(bo.getKey());
            }
        }
        Query totalQuery = session.createSQLQuery("select count(rd.name) " + coreSql);
        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order o = Pager.getOrder().next();
            coreSql += " order by rd." + o.getName() + (o.isReverse() ? " desc " : " asc ");
        } else {
            coreSql += " order by rd.key1 asc ";
        }
        Query query = session.createSQLQuery("select rd.* " + coreSql);
        if (CollectionUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.size(); i++) {
                totalQuery.setParameter(i, params.get(i));
                query.setParameter(i, params.get(i));
            }
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        BigInteger bigInteger = (BigInteger) totalQuery.uniqueResult();
        if (bigInteger == null || bigInteger.intValue() == 0) {
            vo.setTotal(0L);
            return vo;
        }
        vo.setTotal(bigInteger.longValue());
        query.setFirstResult(start);
        query.setMaxResults(limit);
        List<Map<String, Object>> data = query.list();
        vo.setData(data);
        return vo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> report6(StockWeekBo bo) {
        Session session = HibernateUtils.getSession(false);
        String sql = "select s_key as key1,code," +
                "sum(case isYang when 1 then 1 else 0 end) as yang," +
                "sum(nextHigh) as nextHigh,sum(nextLow) as nextLow,count(id) as counts " +
                "from stock_week where  nextHigh is not null ";
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotEmpty(bo.getCode())) {
            sql += " and code=? ";
            params.add(bo.getCode());
        }
        if (StringUtils.isNotEmpty(bo.getKey())) {
            sql += " and s_key=? ";
            params.add(bo.getKey());
        }
        sql += " group by s_key,code ";
        sql = "select t.*,t.nextHigh/t.counts as avgHigh,t.nextLow/t.counts as avgLow,t.yang/t.counts as per from (" + sql + ") t ";
        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order o = Pager.getOrder().next();
            sql += " order by " + o.getName() + (o.isReverse() ? " desc " : " asc ");
        } else {
            sql += " order by t.key1 asc ";
        }
        Query query = session.createSQLQuery(sql);
        int index = 0;
        for (Object o : params) {
            query.setParameter(index++, o);
        }
        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(IntegerUtils.add(Pager.getStart()))
                .setMaxResults(IntegerUtils.add(Pager.getLimit()))
                .list();
    }


    @Override
    public void doCallback(StockWeek stockWeek, StockWeekVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

    }
}
