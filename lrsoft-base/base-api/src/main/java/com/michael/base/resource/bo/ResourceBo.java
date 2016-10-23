package com.michael.base.resource.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.LikeModel;
import com.michael.core.hibernate.criteria.MatchModel;

/**
 * @author Michael
 */
public class ResourceBo implements BO {

    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String name;

    @Condition
    private String code;

    @Condition
    private String parentId;

    @Condition(matchMode = MatchModel.NULL, target = "parentId")
    private Boolean isParent;

    @Condition
    private String type;

    @Condition
    private Boolean deleted;

    @Condition
    private Boolean authorization;

    public Boolean getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Boolean authorization) {
        this.authorization = authorization;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }
}
