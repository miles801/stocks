package com.michael.base.parameter.domain;

import com.michael.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author miles
 * @datetime 2014-06-20
 */
@Entity
@Table(name = "sys_sp_item")
public class SysParamItem extends CommonDomain {

    @NotNull
    @Column(length = 40, nullable = false)
    private String name;
    @NotNull
    @Column(length = 40, nullable = false)
    private String value;
    @NotNull
    @Column(length = 40)
    private String status;
    /**
     * 所属类型的编号
     */
    @NotNull
    @Column(name = "param_type", length = 40, nullable = false)
    private String type;

    /**
     * 级联类型的编号
     */
    @Column(name = "cascade_type_code", length = 40)
    private String cascadeTypeCode;
    /**
     * 级联类型选项的值
     */
    @Column(name = "cascade_item_value", length = 40)
    private String cascadeItemValue;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCascadeTypeCode() {
        return cascadeTypeCode;
    }

    public void setCascadeTypeCode(String cascadeTypeCode) {
        this.cascadeTypeCode = cascadeTypeCode;
    }

    public String getCascadeItemValue() {
        return cascadeItemValue;
    }

    public void setCascadeItemValue(String cascadeItemValue) {
        this.cascadeItemValue = cascadeItemValue;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
}
