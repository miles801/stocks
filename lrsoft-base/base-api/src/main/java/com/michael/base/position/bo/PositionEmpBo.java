package com.michael.base.position.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;

/**
 * @author Michael
 */
public class PositionEmpBo implements BO {

    @Condition
    private String positionId;

    @Condition
    private String empId;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}
