package com.michael.stock.db.domain;

import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Fn数据库
 *
 * @author Michael
 */
@Entity
@Table(name = "stock_db_fn")
public class FnDB extends CommonDomain {
    @ApiField(value = "所属数据库", desc = "参数:DB_TYPE")
    @NotNull
    @Column(name = "type", nullable = false, length = 40)
    private String type;

    @ApiField(value = "日期")
    @NotNull
    @Column(name = "fnDate", nullable = false)
    private Date fnDate;

    @ApiField(value = "fn系数")
    @NotNull
    @Column(name = "fn", nullable = false)
    private Integer fn;

    @ApiField(value = "原始日期")
    @NotNull
    @Column(name = "originDate", nullable = false)
    private Date originDate;


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setFnDate(Date fnDate) {
        this.fnDate = fnDate;
    }

    public Date getFnDate() {
        return this.fnDate;
    }

    public void setFn(Integer fn) {
        this.fn = fn;
    }

    public Integer getFn() {
        return this.fn;
    }

    public void setOriginDate(Date originDate) {
        this.originDate = originDate;
    }

    public Date getOriginDate() {
        return this.originDate;
    }


}
