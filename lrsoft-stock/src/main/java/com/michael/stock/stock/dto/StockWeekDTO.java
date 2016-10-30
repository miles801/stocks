package com.michael.stock.stock.dto;

import com.michael.poi.annotation.Col;
import com.michael.poi.annotation.ImportConfig;
import com.michael.poi.core.DTO;

import java.util.Date;

/**
 * @author Michael
 */
@ImportConfig(file = "", startRow = 1)
public class StockWeekDTO implements DTO {
    // 股票编号
    @Col(index = 0)
    private String code;
    // 股票名称
    @Col(index = 1)
    private String name;
    // 日期
    @Col(index = 2)
    private Date businessDate;
    // 代码
    @Col(index = 3)
    private String key;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public Date getBusinessDate() {
        return this.businessDate;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
