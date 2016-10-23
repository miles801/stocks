package com.michael.base.parameter.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;

/**
 * @author miles
 * @datetime 2014-06-20
 */

public class SysParamItemBo implements BO {
    @Condition
    private String code;

    @Condition
    private String name;

    @Condition
    private String type;


    @Condition
    private String parentId;

    @Condition
    private String status;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
