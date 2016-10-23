package com.michael.base.parameter.domain;

import com.michael.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 系统参数类型
 *
 * @author miles
 * @datetime 2014-06-20
 */
@Entity
@Table(name = "sys_sp_type")
public class SysParamType extends CommonDomain {


    @Column(length = 40, nullable = false)
    private String name;
    @Column(length = 40, nullable = false)
    private String code;
    @Column(name = "parent_id", length = 40)
    private String parentId;
    @Column(name = "parent_name", length = 40)
    private String parentName;
    @Column(name = "sequence_no", length = 40)
    private Integer sequenceNo;
    @Column(length = 40)
    private String status;
    @Column(length = 2000)
    private String path;
    @Transient
    private List<SysParamType> children;

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
    public List<SysParamType> getChildren() {
        return children;
    }

    public void setChildren(List<SysParamType> children) {
        this.children = children;
    }
}
