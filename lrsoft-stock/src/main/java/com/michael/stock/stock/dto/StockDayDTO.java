package com.michael.stock.stock.dto;

import com.michael.poi.annotation.Col;
import com.michael.poi.annotation.ImportConfig;
import com.michael.poi.core.DTO;

import java.util.Date;
/**
 * @author Michael
 */
@ImportConfig(file = "", startRow = 1)
public class StockDayDTO implements DTO {
    // 股票代码
    @Col(index=0)
    private String code;
    // 股票名称
    @Col(index=1)
    private String name;
    // 日期
    @Col(index=2)
    private Date businessDate;
    // 代码
    @Col(index=3)
    private String key;
    // 开盘价
    @Col(index=4)
    private Double openPrice;
    // 收盘价
    @Col(index=5)
    private Double closePrice;
    // 昨日收盘价
    @Col(index=6)
    private Double yestodayClosePrice;
    // 今日涨跌
    @Col(index=7)
    private Double updown;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setBusinessDate(Date businessDate){
        this.businessDate = businessDate;
    }
    public Date getBusinessDate(){
        return this.businessDate;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getKey(){
        return this.key;
    }
    public void setOpenPrice(Double openPrice){
        this.openPrice = openPrice;
    }
    public Double getOpenPrice(){
        return this.openPrice;
    }
    public void setClosePrice(Double closePrice){
        this.closePrice = closePrice;
    }
    public Double getClosePrice(){
        return this.closePrice;
    }
    public void setYestodayClosePrice(Double yestodayClosePrice){
        this.yestodayClosePrice = yestodayClosePrice;
    }
    public Double getYestodayClosePrice(){
        return this.yestodayClosePrice;
    }
    public void setUpdown(Double updown){
        this.updown = updown;
    }
    public Double getUpdown(){
        return this.updown;
    }
}
