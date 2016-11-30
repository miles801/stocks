package com.michael.stock.fn.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.MatchModel;

import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
public class Fn4Bo implements BO {
    // a1
    @Condition
    private Date a1;

    // a2
    @Condition
    private Date a2;

    // a3
    @Condition
    private Date a3;

    // a4
    @Condition
    private Date a4;

    // bk
    @Condition
    private Date bk;
    @Condition(matchMode = MatchModel.GE, target = "bk")
    private Date bkGe;
    @Condition(matchMode = MatchModel.LE, target = "bk")
    private Date bkLe;

    // fn
    @Condition
    private Integer fn;
    @Condition(matchMode = MatchModel.GE, target = "fn")
    private Integer fnGe;
    @Condition(matchMode = MatchModel.LE, target = "fn")
    private Integer fnLe;
    // 类型
    @Condition
    private Integer type;
    @Condition(matchMode = MatchModel.IN, target = "type")
    private List<Integer> typeIn;

    public Date getBkGe() {
        return bkGe;
    }

    public void setBkGe(Date bkGe) {
        this.bkGe = bkGe;
    }

    public Date getBkLe() {
        return bkLe;
    }

    public void setBkLe(Date bkLe) {
        this.bkLe = bkLe;
    }

    public Integer getFnGe() {
        return fnGe;
    }

    public void setFnGe(Integer fnGe) {
        this.fnGe = fnGe;
    }

    public Integer getFnLe() {
        return fnLe;
    }

    public void setFnLe(Integer fnLe) {
        this.fnLe = fnLe;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getTypeIn() {
        return typeIn;
    }

    public void setTypeIn(List<Integer> typeIn) {
        this.typeIn = typeIn;
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
