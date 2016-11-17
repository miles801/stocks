package com.michael.stock.stock.schedule;

import com.michael.core.SystemContainer;
import com.michael.stock.stock.service.StockWeekService;
import org.apache.log4j.Logger;

/**
 * 同步股票交易的线程，实际上就是调用StockService
 *
 * @author Michael
 */
public class StockWeekThread implements Runnable {

    private String[] codes;

    public StockWeekThread(String[] codes) {
        this.codes = codes;
    }

    @Override
    public void run() {
        Logger logger = Logger.getLogger(StockWeekThread.class);
        StockWeekService stockWeekService = SystemContainer.getInstance().getBean(StockWeekService.class);
        for (String code : codes) {
            logger.info("初始化股票交易周K历史 - start ：" + code);
            stockWeekService.reset(code);
            logger.info("初始化股票交易周K历史 - end ：" + code);
        }
    }

}
