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

    // 同步股票交易数据
    @Scheduled(cron = "0 30 15 * * ?")
    @SuppressWarnings("unchecked")
    public void execute() {
        Logger logger = Logger.getLogger(StockBusinessSchedule.class);
        logger.info("****************** 同步股票交易数据:start ******************");
        try (Session session = HibernateUtils.openSession()) {
            // 清除今天的交易数据
            session.createQuery("delete from " + StockDay.class.getName() + " s where s.businessDate=?")
                    .setParameter(0, DateUtils.getDayBegin(new Date()))
                    .executeUpdate();

            // 获取所有股票编号
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
                logger.info("同步股票交易历史 - start ：" + StringUtils.join(cs, " ; "));
                stockDayService.syncStockBusiness(cs);
                logger.info("同步股票交易历史 - end ：" + StringUtils.join(cs, " ; "));
            }
        }
        logger.info("****************** 同步股票交易数据:end ******************");
    }


    /**
     * 重置第7日信息
     */
    @SuppressWarnings("unchecked")
    public void reset7DayInfo() {
        Logger logger = Logger.getLogger(StockBusinessSchedule.class);
        logger.info("****************** 重置第7日信息:start ******************");
        try (Session session = HibernateUtils.openSession()) {

            // 获取所有股票编号
            List<String> codes = session.createQuery("select o.code from " + Stock.class.getName() + " o ")
                    .list();
            StockDayService stockDayService = SystemContainer.getInstance().getBean(StockDayService.class);
            // 循环重置
            for (String code : codes) {
                stockDayService.reset7DayInfo(code);
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
}
