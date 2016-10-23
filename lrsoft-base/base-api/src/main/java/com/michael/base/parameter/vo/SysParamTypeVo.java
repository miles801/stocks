package com.michael.base.parameter.vo;


import com.michael.common.CommonVo;

import java.util.List;

/**
 * @author miles
 * @datetime 2014-06-20
 */

public class SysParamTypeVo extends CommonVo {

    private String name;
    private String code;
    private String parentId;
    private String parentName;
    private Integer sequenceNo;
    private String statusName;
    private String path;
    private List<SysParamTypeVo> children;

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

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<SysParamTypeVo> getChildren() {
        return children;
    }

    public void setChildren(List<SysParamTypeVo> children) {
        this.children = children;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
