package com.michael.base.org.domain;

import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;
import com.michael.tree.Tree;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
/**
 * @author Michael
 */

/**
 * 组织机构
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_org")
public class Org extends CommonDomain implements Tree {

    @ApiField("机构名称")
    @NotNull
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @ApiField(value = "编号", desc = "用于在新建用户时默认指定某个机构，可以为空，如果有值则必须唯一")
    @Column(length = 20, unique = true)
    private String code;

    @ApiField(value = "机构长名称", desc = "实际上是导航名称（从上级到本级的机构的名称）")
    @Column(length = 200, nullable = false, unique = true)
    private String longName;

    @ApiField(value = "机构简拼缩写", desc = "一般用于搜索")
    @Column(length = 20)
    private String pinyin;

    // 上级
    @Column(length = 40)
    private String parentId;

    @Column(length = 40)
    private String parentName;


    @ApiField(value = "访问路径")
    @Column(length = 400)
    private String path;

    @ApiField(value = "层级", desc = "该值由后台自动设置,最小值为0，表示首层")
    @Column
    @Min(value = 0)
    @Max(value = 20)
    private Integer level;


    @ApiField(value = "排序号")
    @Column
    private Integer sequenceNo;

    @ApiField(value = "是否禁用")
    @NotNull
    @Column
    private Boolean deleted;

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

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
