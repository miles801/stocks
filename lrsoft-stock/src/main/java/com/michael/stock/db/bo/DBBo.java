package com.michael.stock.db.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.MatchModel;

import java.util.Date;

/**
 * @author Michael
 */
public class DBBo implements BO {
    // 类型
    @Condition
    private String type;

    // 时间
    @Condition
    private Date dbDate;
    @Condition(matchMode = MatchModel.GE, target = "dbDate")
    private Date dbDateGe;
    @Condition(matchMode = MatchModel.LT, target = "dbDate")
    private Date dbDateLt;

    // 日期2
    @Condition
    private Date dbDate2;
    @Condition(matchMode = MatchModel.GE, target = "dbDate")
    private Date dbDate2Ge;
    @Condition(matchMode = MatchModel.LT, target = "dbDate")
    private Date dbDate2Lt;


    public Date getDbDateGe() {
        return dbDateGe;
    }

    public void setDbDateGe(Date dbDateGe) {
        this.dbDateGe = dbDateGe;
    }

    public Date getDbDateLt() {
        return dbDateLt;
    }

    public void setDbDateLt(Date dbDateLt) {
        this.dbDateLt = dbDateLt;
    }

    public Date getDbDate2Ge() {
        return dbDate2Ge;
    }

    public void setDbDate2Ge(Date dbDate2Ge) {
        this.dbDate2Ge = dbDate2Ge;
    }

    public Date getDbDate2Lt() {
        return dbDate2Lt;
    }

    public void setDbDate2Lt(Date dbDate2Lt) {
        this.dbDate2Lt = dbDate2Lt;
    }

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
