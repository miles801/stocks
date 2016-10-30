package com.michael.stock.stock.domain;

import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 周K
 *
 * @author Michael
 */
@Entity
@Table(name = "stock_week")
public class StockWeek extends CommonDomain {
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

    @ApiField(value = "代码")
    @NotNull
    @Column(name = "s_key", nullable = false, length = 10)
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
