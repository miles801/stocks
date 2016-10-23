package com.michael.stock.stock.schedule;

import com.michael.core.SystemContainer;
import com.michael.stock.stock.service.StockService;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 同步股票的定时器
 * 只是同步股票的信息（默认每天早上10点启动）
 *
 * @author Michael
 */
@Component
public class StockSchedule {

    @Scheduled(cron = "0 0 10 * * ?")
    public void execute() {
        Logger logger = Logger.getLogger(StockSchedule.class);
        logger.info("****************** 股票同步:start ******************");
        StockService stockService = SystemContainer.getInstance().getBean(StockService.class);
        stockService.syncStock();
        logger.info("****************** 股票同步:end ******************");
    }
}
