package com.michael.base.parameter.vo;


import com.michael.common.CommonVo;

import java.util.List;

/**
 * @author miles
 * @datetime 2014-07-02
 */

public class BusinessParamTypeVo extends CommonVo {

    private String name;
    private String code;
    private String parentId;
    private String parentName;
    private Integer sequenceNo;
    private String statusName;
    private String path;

    private List<BusinessParamTypeVo> children;

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

    public List<BusinessParamTypeVo> getChildren() {
        return children;
    }

    public void setChildren(List<BusinessParamTypeVo> children) {
        this.children = children;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
