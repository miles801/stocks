package com.michael.stock.fn.domain;

import com.michael.docs.annotations.ApiField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Fn5
 *
 * @author Michael
 */
@Entity
@Table(name = "stock_fn5")
public class Fn5 {
    @ApiField(value = "ID")
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.michael.utils.SnowflakeIDStrategy")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id", unique = true, nullable = false, length = 40)
    private String id;

    @ApiField(value = "a1")
    @NotNull
    @Column(name = "a1", nullable = false)
    private Date a1;

    @ApiField(value = "a2")
    @NotNull
    @Column(name = "a2", nullable = false)
    private Date a2;

    @ApiField(value = "a3")
    @NotNull
    @Column(name = "a3", nullable = false)
    private Date a3;

    @ApiField(value = "a4")
    @NotNull
    @Column(name = "a4", nullable = false)
    private Date a4;

    @ApiField(value = "a5")
    @NotNull
    @Column(name = "a5", nullable = false)
    private Date a5;

    @ApiField(value = "bk")
    @NotNull
    @Column(name = "bk", nullable = false)
    private Date bk;

    @ApiField(value = "偏移值")
    @NotNull
    @Column(name = "fn", nullable = false)
    private Integer fn;

    @ApiField(value = "数据库类型")
    @NotNull
    @Column(name = "type", nullable = false)
    private Integer type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setA1(Date a1) {
        this.a1 = a1;
    }

    public Date getA1() {
        return this.a1;
    }

    public void setA2(Date a2) {
        this.a2 = a2;
    }

    public Date getA2() {
        return this.a2;
    }

    public void setA3(Date a3) {
        this.a3 = a3;
    }

    public Date getA3() {
        return this.a3;
    }

    public void setA4(Date a4) {
        this.a4 = a4;
    }

    public Date getA4() {
        return this.a4;
    }

    public void setA5(Date a5) {
        this.a5 = a5;
    }

    public Date getA5() {
        return this.a5;
    }

    public void setBk(Date bk) {
        this.bk = bk;
    }

    public Date getBk() {
        return this.bk;
    }

    public void setFn(Integer fn) {
        this.fn = fn;
    }

    public Integer getFn() {
        return this.fn;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }


}
