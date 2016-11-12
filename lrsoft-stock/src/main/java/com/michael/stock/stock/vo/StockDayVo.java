package com.michael.stock.stock.vo;

import com.michael.stock.stock.domain.StockDay;

/**
 * @author Michael
 */
public class StockDayVo extends StockDay {
    private String percent;

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

}
