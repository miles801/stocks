package com.michael.stock.stock.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;

/**
 * @author Michael
 */
public class StockDayBo implements BO{
    // 股票编号
    @Condition
    private String code;

    // 股票名称
    @Condition
    private String name;

    // 代码
    @Condition
    private String key;

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
    public void setKey(String key){
        this.key = key;
    }
    public String getKey(){
        return this.key;
    }
}
