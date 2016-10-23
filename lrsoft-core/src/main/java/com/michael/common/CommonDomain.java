package com.michael.common;

import com.michael.docs.annotations.ApiField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 实体类的基类，提供了常用的一些属性
 * ID、创建人、创建时间、修改人、修改时间
 * Created by Michael on 2014/10/17.
 */
@MappedSuperclass
public class CommonDomain {


    @ApiField(value = "ID")
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.michael.utils.SnowflakeIDStrategy")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id", unique = true, nullable = false, length = 40)
    private String id;

    @ApiField(value = "创建人/录入人")
    @Column(length = 40)
    private String creatorId;
    @ApiField(value = "创建人名称/录入人名称")
    @Column(length = 40)
    private String creatorName;
    @ApiField(value = "修改人")
    @Column(length = 40)
    private String modifierId;
    @ApiField(value = "修改人名称")
    @Column(length = 40)
    private String modifierName;
    @ApiField(value = "创建时间")
    @Column
    private Date createdDatetime;
    @ApiField(value = "修改时间")
    @Column
    private Date modifiedDatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

}
