package com.michael.base.resource.domain;

import com.michael.base.attachment.AttachmentSymbol;
import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 系统资源
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_resource")
public class Resource extends CommonDomain implements AttachmentSymbol {


    /**
     * 资源类型
     */
    public static final String PARAM_TYPE = "SP_RESOURCE_TYPE";
    /**
     * 系统参数：模块
     */
    public static final String SYS_MODULE = "SP_MODULE";

    /**
     * URL
     */
    public static final String TYPE_MENU = "MENU";
    /**
     * ELEMENT
     */
    public static final String TYPE_ELEMENT = "ELEMENT";
    /**
     * DATA
     */
    public static final String TYPE_DATA = "DATA";


    @ApiField(value = "资源名称")
    @NotNull
    @Column(length = 40, nullable = false)
    private String name;

    @ApiField(value = "资源编号")
    @NotNull
    @Column(length = 40, nullable = false, unique = true)
    private String code;

    @ApiField(value = "所属模块", desc = "资源类型是TYPE_ELEMENT时，必须设置该字段。该字段来自于系统参数：SP_MODULE")
    @Column(length = 40)
    private String module;

    @ApiField(value = "资源类型", desc = "菜单(MENU)、元素(ELEMENT)、数据(DATA)")
    @NotNull
    @Column(length = 40, nullable = false)
    private String type;


    @ApiField(value = "是否需要授权")
    @Column
    private Boolean authorization;

    @ApiField(value = "排序号，从0开始")
    @Column
    private Integer sequenceNo;

    @ApiField(value = "资源的URL，当类型是MENU时有效")
    @Column(length = 100)
    private String url;

    @ApiField(value = "图标ID，当为子系统时需要")
    @Column(length = 40)
    private String icon;

    @ApiField(value = "上级ID")
    @Column(length = 40)
    private String parentId;

    @ApiField(value = "上级名称")
    @Column(length = 40)
    private String parentName;

    @ApiField(value = "资源的描述信息")
    @Column(length = 1000)
    private String description;

    @ApiField(value = "是否禁用")
    @Column
    private Boolean deleted;

    @Override
    public String businessId() {
        return getId();
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public Boolean getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Boolean authorization) {
        this.authorization = authorization;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
