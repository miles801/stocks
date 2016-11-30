package com.michael.stock.fn.domain;

import com.michael.docs.annotations.ApiField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 四元计算
 *
 * @author Michael
 */
@Entity
@Table(name = "stock_fn4")
public class Fn4 {

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

    @ApiField(value = "bk")
    @NotNull
    @Column(name = "bk", nullable = false)
    private Date bk;

    @ApiField(value = "fn")
    @NotNull
    @Column(name = "fn", nullable = false)
    private Integer fn;

    @Column
    private Integer type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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


}
