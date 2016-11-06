package com.michael.stock.stock.domain;

import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 日K
 *
 * @author Michael
 */
@Entity
@Table(name = "stock_day")
public class StockDay extends CommonDomain {
    @ApiField(value = "股票编号")
    @NotNull
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @ApiField(value = "股票名称")
    @NotNull
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @ApiField(value = "日期")
    @NotNull
    @Column(name = "businessDate", nullable = false)
    private Date businessDate;

    @ApiField(value = "6线组合代码")
    @NotNull
    @Column(name = "s_key", nullable = false, length = 10)

    private String key;
    @ApiField(value = "6线组合开始时间")
    @Column
    private Date date6;

    @ApiField(value = "3线组合代码")
    @Column(length = 3, name = "s_key3")
    private String key3;

    @ApiField(value = "3线组合开始时间")
    @Column
    private Date date3;

    @ApiField("第1日")
    @Column
    private Double p1;
    @ApiField("第2日")
    @Column
    private Double p2;
    @ApiField("第3日")
    @Column
    private Double p3;
    @ApiField("第4日")
    @Column
    private Double p4;
    @ApiField("第5日")
    @Column
    private Double p5;
    @ApiField("第6日")
    @Column
    private Double p6;
    @ApiField("下一日高")
    @Column
    private Double nextHigh;
    @ApiField("下一日低")
    @Column
    private Double nextLow;

    @ApiField("下一日是否为阳")
    @Column
    private Boolean isYang;

    @ApiField(value = "开盘价")
    @Column(name = "openPrice")
    private Double openPrice;

    @ApiField(value = "最高价")
    @Column
    private Double highPrice;
    @ApiField(value = "最低价")
    @Column
    private Double lowPrice;

    @ApiField(value = "收盘价")
    @Column(name = "closePrice")
    private Double closePrice;

    @ApiField(value = "昨日收盘价")
    @Column(name = "yesterdayClosePrice")
    private Double yesterdayClosePrice;

    @ApiField(value = "今日涨跌")
    @Column(name = "s_updown")
    private Double updown;

    public Boolean getYang() {
        return isYang;
    }

    public void setYang(Boolean yang) {
        isYang = yang;
    }

    public Date getDate6() {
        return date6;
    }

    public void setDate6(Date date6) {
        this.date6 = date6;
    }

    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    public Double getP1() {
        return p1;
    }

    public void setP1(Double p1) {
        this.p1 = p1;
    }

    public Double getP2() {
        return p2;
    }

    public void setP2(Double p2) {
        this.p2 = p2;
    }

    public Double getP3() {
        return p3;
    }

    public void setP3(Double p3) {
        this.p3 = p3;
    }

    public Double getP4() {
        return p4;
    }

    public void setP4(Double p4) {
        this.p4 = p4;
    }

    public Double getP5() {
        return p5;
    }

    public void setP5(Double p5) {
        this.p5 = p5;
    }

    public Double getP6() {
        return p6;
    }

    public void setP6(Double p6) {
        this.p6 = p6;
    }

    public Double getNextHigh() {
        return nextHigh;
    }

    public void setNextHigh(Double nextHigh) {
        this.nextHigh = nextHigh;
    }

    public Double getNextLow() {
        return nextLow;
    }

    public void setNextLow(Double nextLow) {
        this.nextLow = nextLow;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

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

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getOpenPrice() {
        return this.openPrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getClosePrice() {
        return this.closePrice;
    }

    public Double getYesterdayClosePrice() {
        return yesterdayClosePrice;
    }

    public void setYesterdayClosePrice(Double yesterdayClosePrice) {
        this.yesterdayClosePrice = yesterdayClosePrice;
    }

    public void setUpdown(Double updown) {
        this.updown = updown;
    }

    public Double getUpdown() {
        return this.updown;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }
}
