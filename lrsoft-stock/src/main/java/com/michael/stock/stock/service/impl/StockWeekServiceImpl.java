package com.michael.stock.stock.service.impl;

import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.stock.stock.bo.StockWeekBo;
import com.michael.stock.stock.dao.StockWeekDao;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.domain.StockWeek;
import com.michael.stock.stock.service.StockWeekService;
import com.michael.stock.stock.vo.StockWeekVo;
import com.michael.utils.collection.CollectionUtils;
import com.michael.utils.date.DateUtils;
import com.michael.utils.string.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        Date date = DateUtils.getDate(1970, 1, 1);  // 相当于从最开始开始
        Date today = DateUtils.getDayBegin(new Date());
        int index = 0;
        // 最近5周的数组
        List<StockWeek> weeks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            StockWeek week = new StockWeek();
            week.setOpenDate(today);
            week.setCloseDate(today);
            week.setOpenPrice(0d);
            week.setClosePrice(0d);
            week.setYesterdayClosePrice(0d);
            week.setDate3(today);
            week.setDate6(today);
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
            weeks.add(week);
        }

        while (true) {
            // 取出最早的5只股票
            List<StockDay> stockDays = session.createQuery("from " + StockDay.class.getName() + " s where s.businessDate>? and  s.code=? order by s.businessDate asc")
                    .setParameter(0, date)
                    .setParameter(1, stockCode)
                    .setMaxResults(5)
                    .list();
            if (CollectionUtils.isEmpty(stockDays)) {
                break;  // 如果已经没有股票了则退出循环
            }

            // 如果最后一个日期的周数和今天的年周数一致,且今天不是周五，则表明读取到了最后
            if (DateUtils.getYearWeek(date) == DateUtils.getWeek(today) && today.getYear() == date.getYear() && DateUtils.getWeek(today) != Calendar.FRIDAY) {
                break;
            }


            // 获取本周的基本数据
            double highPrice = 0d;   // 最高价
            double lowPrice = 0d;    // 最低价
            double openPrice = 0d;     // 开盘价
            double closePrice = 0d;     // 开盘价
            Date openDate = null;     // 开盘日期
            Date closeDate = null;    // 收盘日期
            int openTimes = 0; // 这一周的开盘天数
            int weekNo = 0;
            for (StockDay day : stockDays) {
                Date businessDate = day.getBusinessDate();
                int nowWeekDay = DateUtils.getWeek(businessDate);
                if (nowWeekDay < weekNo) {  // 如果取出的星期要小于上一个星期，则表示进入下一周了，已经取满本周数据
                    break;
                }
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
            stockWeek.setUpdown( closePrice-openPrice);

            // 设置3周数据
            final StockWeek lastWeek = weeks.get(4);
            stockWeek.setKey3(lastWeek.getKey3().substring(1) + (stockWeek.getUpdown() > 0 ? "1" : "0"));
            stockWeek.setDate3(weeks.get(3).getOpenDate());
            // 设置6周数据
            stockWeek.setKey(lastWeek.getKey().substring(1) + (stockWeek.getUpdown() > 0 ? "1" : "0"));
            stockWeek.setDate6(weeks.get(0).getOpenDate());

            // 7周阴阳
            lastWeek.setYang(stockWeek.getUpdown() > 0);

            // 第1周
            stockWeek.setYesterdayClosePrice(lastWeek.getClosePrice());
            if (lastWeek.getClosePrice() != 0d) {
                stockWeek.setP1((stockWeek.getClosePrice() - lastWeek.getClosePrice()) / lastWeek.getClosePrice());
                // 第2周
                stockWeek.setP2((stockWeek.getClosePrice() - weeks.get(1).getClosePrice()) / lastWeek.getClosePrice());
                // 第3周
                stockWeek.setP3((stockWeek.getClosePrice() - weeks.get(2).getClosePrice()) / lastWeek.getClosePrice());
                // 第4周
                stockWeek.setP4((stockWeek.getClosePrice() - weeks.get(3).getClosePrice()) / lastWeek.getClosePrice());
                // 第5周
                stockWeek.setP5((stockWeek.getClosePrice() - weeks.get(4).getClosePrice()) / lastWeek.getClosePrice());

                // 第七周高
                lastWeek.setNextHigh(stockWeek.getHighPrice() - lastWeek.getClosePrice() / lastWeek.getClosePrice());
                // 第七周低
                lastWeek.setNextLow(stockWeek.getLowPrice() - lastWeek.getClosePrice() / lastWeek.getClosePrice());

                if (StringUtils.isNotEmpty(lastWeek.getId())) {
                    session.update(lastWeek);
                }
            }
            weeks.remove(0);
            String id = (String) session.save(stockWeek);
            stockWeek.setId(id);
            weeks.add(stockWeek);

            if (index % 10 == 0) {
                session.flush();
                session.clear();
            }

            date = closeDate;   // 重置
        }
    }

    @Override
    public void add(String stockCode) {
        Assert.hasText(stockCode, "操作失败!股票代码不能为空!");
    }

    @Override
    public void doCallback(StockWeek stockWeek, StockWeekVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

    }
}
