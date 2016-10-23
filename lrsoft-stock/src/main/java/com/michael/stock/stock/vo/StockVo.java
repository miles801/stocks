package com.michael.stock.stock.vo;

import com.michael.stock.stock.domain.Stock;

/**
 * @author Michael
 */
public class StockVo extends Stock {
     // 状态--参数名称
     private String statusName;

    public void setStatusName(String statusName){
        this.statusName = statusName;
    }
    public String getStatusName(){
       return this.statusName;
    }
}
