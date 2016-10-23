package com.michael.base.region.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.LikeModel;
import com.michael.core.hibernate.criteria.MatchModel;

/**
 * @author miles
 * @datetime 2014-03-25
 */

public class RegionBo implements BO {
    @Condition
    private Integer type;
    /**
     * 名称
     */
    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String name;


    /**
     * 区号
     */
    @Condition
    private String code;
    /**
     * 邮编
     */
    @Condition
    private String zipcode;
    @Condition
    private String parentId;
    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String parentName;

    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String pinyin;
    /**
     * 简拼
     */
    @Condition
    private String jp;

    /**
     * 是否删除
     */
    @Condition
    private Boolean deleted;

    @Condition
    private String masterId;

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getJp() {
        return jp;
    }

    public void setJp(String jp) {
        this.jp = jp;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
