package com.michael.stock.stock.schedule;

import com.michael.core.SystemContainer;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.stock.stock.domain.Stock;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.service.StockDayService;
import com.michael.stock.stock.service.StockService;
import com.michael.utils.date.DateUtils;
import com.michael.utils.number.IntegerUtils;
import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 同步股票当天的交易记录的定时器（下午3:30）
 *
 * @author Michael
 */
@Component
public class StockBusinessSchedule {


    /**
     * 同步每日交易数据（每周1-5的下午3点半）
     */
    @Scheduled(cron = "0 30 15 ? * MON-FRI")
    @SuppressWarnings("unchecked")
    public void syncDayBusiness() {
        Logger logger = Logger.getLogger(StockBusinessSchedule.class);
        logger.info("****************** 同步股票交易数据:start ******************");
        try (Session session = HibernateUtils.openSession()) {
            // 清除今天的交易数据
            session.createQuery("delete from " + StockDay.class.getName() + " s where s.businessDate=?")
                    .setParameter(0, DateUtils.getDayBegin(new Date()))
                    .executeUpdate();

            // 获取所有股票代码
            List<String> codes = session.createQuery("select o.code from " + Stock.class.getName() + " o ")
                    .list();
            int size = codes.size();
            final int batch = 50;
            long times = IntegerUtils.times(size, batch);
            StockDayService stockDayService = SystemContainer.getInstance().getBean(StockDayService.class);
            for (int i = 0; i < times; i++) {
                int last = (i + 1) * batch;
                if (last > size) {
                    last = size;
                }
                String[] cs = codes.subList(i * batch, last).toArray(new String[]{});
                logger.info(String.format("同步股票交易历史 - start （%d / %d）：%s", i + 1, times, StringUtils.join(cs, " ; ")));
                stockDayService.syncStockBusiness(cs);
                logger.info(String.format("同步股票交易历史 - end （%d / %d）：%s", i + 1, times, StringUtils.join(cs, " ; ")));
            }
        }
        logger.info("****************** 同步股票交易数据:end ******************");
    }


    /**
     * 重置交易数据外的其他信息（3线、6线等数据）
     */
    @SuppressWarnings("unchecked")
    public void resetOtherInfo() {
        Logger logger = Logger.getLogger(StockBusinessSchedule.class);
        logger.info("****************** 重置第7日信息:start ******************");
        try (Session session = HibernateUtils.openSession()) {

            // 获取所有股票代码
            List<String> codes = session.createQuery("select distinct o.code from " + StockDay.class.getName() + " o group by o.code order by o.code asc")
                    .list();
            StockDayService stockDayService = SystemContainer.getInstance().getBean(StockDayService.class);
            // 循环重置
            int size = codes.size();
            int index = 0;
            for (String code : codes) {
                stockDayService.reset7DayInfo(code);
                logger.info(String.format("重置交易数据计算结果--进度(%s): %d / %d", code, ++index, size));
            }
        }
        logger.info("****************** 重置第7日信息:end ******************");
    }


    // 同步股票
    @Scheduled(cron = "0 0 10 * * ?")
    public void syncStock() {
        Logger logger = Logger.getLogger(StockBusinessSchedule.class);
        logger.info("****************** 同步股票:start ******************");
        StockService stockService = SystemContainer.getInstance().getBean(StockService.class);
        stockService.syncStock();
        logger.info("****************** 同步股票:end ******************");
    }


    // 产生风险结果分析

    /**
     * 重新生成最新的风险结果（默认每周六晚上2点执行）
     */
    @Scheduled(cron = "0 0 2 * * SAT")
    public void resetNewResult() {
        Logger logger = Logger.getLogger(StockBusinessSchedule.class);
        try (Session session = HibernateUtils.openSession()) {
            logger.info("****************** 产生风险估值结果表 6日:start ******************");
            session.createSQLQuery("drop table if EXISTS result_day6").executeUpdate();
            session.createSQLQuery("create table result_day6 " +
                    "select t.*,t.nextHigh/t.counts as avgHigh,t.nextLow/t.counts as avgLow, t.yang/t.counts as per from " +
                    "(select code,s_key as key1,concat(code,':',s_key) as name,sum(case isYang when 1 then 1 else 0 end) as yang,sum(nextHigh) nextHigh,sum(nextLow) nextLow,count(id) counts from stock_day where nextHigh is not null group by code,s_key) t ")
                    .executeUpdate();
            session.createSQLQuery("alter table result_day6 add INDEX index_name(name)").executeUpdate();
            session.createSQLQuery("alter table result_day6 add INDEX index_code(code)").executeUpdate();
            session.createSQLQuery("alter table result_day6 add INDEX index_key(key1)").executeUpdate();
            logger.info("****************** 产生风险估值结果表 6日:end ******************");

            logger.info("****************** 产生风险估值结果表 3日:start ******************");
            session.createSQLQuery("drop table if EXISTS result_day3").executeUpdate();
            session.createSQLQuery("create table result_day3 " +
                    "select t.*,t.nextHigh/t.counts as avgHigh,t.nextLow/t.counts as avgLow, t.yang/t.counts as per from " +
                    "(select code,s_key3 as key1,concat(code,':',s_key3) as name,sum(case isYang when 1 then 1 else 0 end) as yang,sum(nextHigh) nextHigh,sum(nextLow) nextLow,count(id) counts from stock_day where nextHigh is not null  group by code,s_key3) t ")
                    .executeUpdate();
            session.createSQLQuery("alter table result_day3 add INDEX index_name(name)").executeUpdate();
            session.createSQLQuery("alter table result_day3 add INDEX index_code(code)").executeUpdate();
            session.createSQLQuery("alter table result_day3 add INDEX index_key(key1)").executeUpdate();
            logger.info("****************** 产生风险估值结果表 3日:end ******************");
        }
    }

}
