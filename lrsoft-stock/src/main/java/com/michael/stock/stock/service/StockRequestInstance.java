package com.michael.stock.stock.service;

import com.miles.stock.api.StockRequest;
import com.miles.stock.sina.SinaStockRequest;

/**
 * @author Michael
 */
public class StockRequestInstance {
    private static StockRequestInstance ourInstance = new StockRequestInstance();

    public static StockRequestInstance getInstance() {
        return ourInstance;
    }

    private StockRequest stockRequest;

    private StockRequestInstance() {
        stockRequest = new SinaStockRequest();
    }

    public StockRequest getStockRequest() {
        return stockRequest;
    }
}
