package com.michael.base.region.domain;

import com.michael.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author miles
 * @datetime 2014-03-25
 */
@Entity
@Table(name = "sys_region")
public class Region extends CommonDomain {
    /**
     * 国家0、省1、市2、区3...
     */
    @Column(nullable = false)
    @NotNull(message = "行政区域类型不能为空!")
    private Integer type;
    /**
     * 名称
     */
    @Column(length = 40, nullable = false)
    @NotNull(message = "行政区域名称不能为空!")
    private String name;


    /**
     * 区号
     */
    @Column(length = 10)
    private String code;
    /**
     * 邮编
     */
    @Column(length = 6)
    private String zipcode;
    /**
     * 排序号
     */
    @Column
    private Integer sequenceNo;
    @Column(length = 40)
    private String parentId;
    @Column(length = 40)
    private String parentName;

    /**
     * 全拼
     */
    @Column(length = 40)
    private String pinyin;
    /**
     * 简拼
     */
    @Column(length = 10)
    private String jp;

    /**
     * 是否删除
     */
    @Column
    private Boolean deleted;

    // 负责人
    @Column(length = 40)
    private String masterId;
    @Column(length = 40)
    private String masterName;

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
