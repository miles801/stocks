package com.michael.stock.stock.schedule;

import com.michael.core.SystemContainer;
import com.michael.stock.stock.service.StockDayService;
import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;

/**
 * 同步股票交易的线程，实际上就是调用StockService
 *
 * @author Michael
 */
public class StockBusinessThread implements Runnable {

    private String[] codes;

    public StockBusinessThread(String[] codes) {
        this.codes = codes;
    }

    @Override
    public void run() {
        Logger logger = Logger.getLogger(StockBusinessThread.class);
        logger.info("同步股票交易历史 - start ：" + StringUtils.join(codes, " ; "));
        StockDayService stockDayService = SystemContainer.getInstance().getBean(StockDayService.class);
        stockDayService.syncStockBusiness(codes);
        logger.info("同步股票交易历史 - end ：" + StringUtils.join(codes, " ; "));
    }

}
