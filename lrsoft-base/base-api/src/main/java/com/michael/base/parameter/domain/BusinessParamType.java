package com.michael.base.parameter.domain;

import com.michael.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author miles
 * @datetime 2014-07-02
 */
@Entity
@Table(name = "sys_bp_type")
public class BusinessParamType extends CommonDomain {

    @Column(length = 40, nullable = false)
    private String name;
    @Column(length = 40, nullable = false)
    private String code;
    @Column(length = 40, name = "parent_id")
    private String parentId;
    @Column(length = 40, name = "parent_name")
    private String parentName;
    @Column(length = 40, name = "sequence_no")
    private Integer sequenceNo;
    @Column(length = 40)
    private String status;
    @Column(length = 1000)
    private String path;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Transient
    private List<BusinessParamType> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    public List<BusinessParamType> getChildren() {
        return children;
    }

    public void setChildren(List<BusinessParamType> children) {
        this.children = children;
    }
}
