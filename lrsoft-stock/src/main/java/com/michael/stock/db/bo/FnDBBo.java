package com.michael.stock.db.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.MatchModel;

import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
public class FnDBBo implements BO {
    // 所属数据库
    @Condition
    private String type;

    // fn系数
    @Condition
    private Integer fn;

    // 原始日期
    @Condition
    private Date originDate;

    @Condition(matchMode = MatchModel.IN, target = "type")
    private List<String> types;
    // 日期
    @Condition
    private Date fnDate;
    @Condition(matchMode = MatchModel.GE, target = "fnDate")
    private Date fnDateGe;
    @Condition(matchMode = MatchModel.LT, target = "fnDate")
    private Date fnDateLt;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Date getFnDateGe() {
        return fnDateGe;
    }

    public void setFnDateGe(Date fnDateGe) {
        this.fnDateGe = fnDateGe;
    }

    public Date getFnDateLt() {
        return fnDateLt;
    }

    public void setFnDateLt(Date fnDateLt) {
        this.fnDateLt = fnDateLt;
    }

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
