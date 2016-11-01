package com.michael.stock.db.domain;

import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数据库
 *
 * @author Michael
 */
@Entity
@Table(name = "stock_db")
public class DB extends CommonDomain {
    @ApiField(value = "类型", desc = "参数:DB_TYPE")
    @NotNull
    @Column(name = "type", nullable = false, length = 40)
    private String type;

    @ApiField(value = "时间")
    @NotNull
    @Column(name = "dbDate", nullable = false)
    private Date dbDate;

    @ApiField(value = "日期2")
    @Column(name = "dbDate2")
    private Date dbDate2;


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setDbDate(Date dbDate) {
        this.dbDate = dbDate;
    }

    public Date getDbDate() {
        return this.dbDate;
    }

    public void setDbDate2(Date dbDate2) {
        this.dbDate2 = dbDate2;
    }

    public Date getDbDate2() {
        return this.dbDate2;
    }


}
