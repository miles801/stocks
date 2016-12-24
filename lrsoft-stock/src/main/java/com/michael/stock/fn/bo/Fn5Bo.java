package com.michael.stock.fn.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.MatchModel;

import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
public class Fn5Bo implements BO {
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

    @Condition
    private Date a5;

    private Date date1;
    private Date date2;

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

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getA1() {
        return a1;
    }

    public void setA1(Date a1) {
        this.a1 = a1;
    }

    public Date getA2() {
        return a2;
    }

    public void setA2(Date a2) {
        this.a2 = a2;
    }

    public Date getA3() {
        return a3;
    }

    public void setA3(Date a3) {
        this.a3 = a3;
    }

    public Date getA4() {
        return a4;
    }

    public void setA4(Date a4) {
        this.a4 = a4;
    }

    public Date getA5() {
        return a5;
    }

    public void setA5(Date a5) {
        this.a5 = a5;
    }

    public Date getBk() {
        return bk;
    }

    public void setBk(Date bk) {
        this.bk = bk;
    }

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

    public Integer getFn() {
        return fn;
    }

    public void setFn(Integer fn) {
        this.fn = fn;
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
}
