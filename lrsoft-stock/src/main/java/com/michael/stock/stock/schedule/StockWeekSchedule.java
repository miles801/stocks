package com.michael.stock.stock.schedule;

import com.michael.core.SystemContainer;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.service.StockWeekService;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Michael
 */
@Component
public class StockWeekSchedule {

    @SuppressWarnings("unchecked")
    public void execute() {
        Logger logger = Logger.getLogger(StockWeekSchedule.class);
        logger.info("****************** 初始化股票周交易数据:start ******************");
        // 获取所有的股票代码
        try (Session session = HibernateUtils.openSession()) {
            List<String> codes = session.createQuery("select distinct o.code from " + StockDay.class.getName() + " o group by o.code ")
                    .list();
            StockWeekService stockWeekService = SystemContainer.getInstance().getBean(StockWeekService.class);
            int index = 0;
            int size = codes.size();
            for (String code : codes) {
                stockWeekService.reset(code);
                logger.info(String.format("股票周交易数据初始化成功：%s,总进度：%d / %d", code, ++index, size));
            }
        }
        logger.info("****************** 初始化股票周交易数据:start ******************");
    }


    public void add() {
        StockWeekService stockWeekService = SystemContainer.getInstance().getBean(StockWeekService.class);
        stockWeekService.add("000001");
    }


    // 产生风险结果分析
    @Scheduled(cron = "0 0 2 6 * ?")
    public void createTableWeek() {
        Logger logger = Logger.getLogger(StockWeekSchedule.class);
        try (Session session = HibernateUtils.openSession()) {
            logger.info("****************** 产生风险估值结果表 6周:start ******************");
            session.createSQLQuery("drop table if EXISTS result_week6").executeUpdate();
            session.createSQLQuery("create table result_week6 " +
                    "select t.*,t.nextHigh/t.counts as avgHigh,t.nextLow/t.counts as avgLow, t.yang/t.counts as per from " +
                    "(select code,s_key as key1,concat(code,':',s_key) as name,sum(case isYang when 1 then 1 else 0 end) as yang,sum(nextHigh) nextHigh,sum(nextLow) nextLow,count(id) counts from stock_week where nextHigh is not null group by code,s_key) t ")
                    .executeUpdate();
            session.createSQLQuery("alter table result_week6 add INDEX index_name(name)").executeUpdate();
            session.createSQLQuery("alter table result_week6 add INDEX index_code(code)").executeUpdate();
            session.createSQLQuery("alter table result_week6 add INDEX index_key(key1)").executeUpdate();
            logger.info("****************** 产生风险估值结果表 6周:end ******************");

            logger.info("****************** 产生风险估值结果表 3周:start ******************");
            session.createSQLQuery("drop table if EXISTS result_week3").executeUpdate();
            session.createSQLQuery("create table result_week3 " +
                    "select t.*,t.nextHigh/t.counts as avgHigh,t.nextLow/t.counts as avgLow, t.yang/t.counts as per from " +
                    "(select code,s_key3 as key1,concat(code,':',s_key3) as name,sum(case isYang when 1 then 1 else 0 end) as yang,sum(nextHigh) nextHigh,sum(nextLow) nextLow,count(id) counts from stock_week where nextHigh is not null  group by code,s_key3) t ")
                    .executeUpdate();
            session.createSQLQuery("alter table result_week3 add INDEX index_name(name)").executeUpdate();
            session.createSQLQuery("alter table result_week3 add INDEX index_code(code)").executeUpdate();
            session.createSQLQuery("alter table result_week3 add INDEX index_key(key1)").executeUpdate();
            logger.info("****************** 产生风险估值结果表 3周:end ******************");
        }
    }

}
