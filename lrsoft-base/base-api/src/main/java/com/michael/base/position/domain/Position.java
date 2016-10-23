package com.michael.base.position.domain;

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
 * 岗位（角色）
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_position")
public class Position extends CommonDomain implements Tree {

    @ApiField(value = "岗位名称")
    @NotNull
    @Column(nullable = false, length = 40)
    private String name;

    @Column(unique = true, length = 40)
    @ApiField(value = "岗位编号，唯一")
    private String code;

    @ApiField(value = "上级岗位")
    @Column(length = 40)
    private String parentId;

    @ApiField(value = "上级岗位名称")
    @Column(length = 40)
    private String parentName;


    @ApiField("导航路径")
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }


}
