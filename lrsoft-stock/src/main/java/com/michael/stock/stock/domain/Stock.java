package com.michael.stock.stock.domain;

import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 股票：用于记录当前系统中所保存的股票
 *
 * @author Michael
 */
@Entity
@Table(name = "stock_stock")
public class Stock extends CommonDomain {
    @ApiField(value = "编号")
    @NotNull
    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @ApiField(value = "名称")
    @NotNull
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @ApiField(value = "类型")
    @Column(name = "type")
    private Integer type;

    @ApiField(value = "状态", desc = "参数:STOCK_STATUS")
    @Column(name = "status", length = 40)
    private String status;


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

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }


}
