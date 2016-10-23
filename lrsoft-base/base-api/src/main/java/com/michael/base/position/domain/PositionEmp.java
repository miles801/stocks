package com.michael.base.position.domain;

import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 岗位员工，保存的是岗位与员工之间的关系
 * 当往岗位添加或移除一个员工时，同时需要校验岗位对员工数量的限制，并更新岗位的员工数量
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_position_emp")
public class PositionEmp extends CommonDomain {

    @ApiField(value = "员工ID")
    @NotNull
    @Column(length = 40, nullable = false)
    private String empId;

    @ApiField(value = "岗位ID")
    @NotNull
    @Column(length = 40, nullable = false)
    private String positionId;

    public PositionEmp() {
    }

    public PositionEmp(String positionId, String empId) {
        this.empId = empId;
        this.positionId = positionId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}
