package com.michael.stock.stock.schedule;

import com.michael.core.SystemContainer;
import com.michael.stock.stock.service.StockService;

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
        StockService stockService = SystemContainer.getInstance().getBean(StockService.class);
        stockService.syncStockBusiness(codes);
    }

}
