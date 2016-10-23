package com.michael.base.emp.domain;

import com.michael.base.attachment.AttachmentSymbol;
import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 员工
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_emp")
public class Emp extends CommonDomain implements AttachmentSymbol {

    /**
     * 正常
     */
    public static final Integer STATUS_NORMAL = 0;
    /**
     * 锁定
     */
    public static final Integer STATUS_LOCKED = 1;
    /**
     * 离职
     */
    public static final Integer STATUS_LEAVE = 2;


    @ApiField(value = "姓名", required = true)
    @Column(nullable = false, length = 40)
    private String name;

    @ApiField(value = "工号", desc = "唯一")
    @Column(length = 40, unique = true)
    private String code;

    @ApiField(value = "登录名", required = true, desc = "唯一")
    @Column(unique = true, length = 40, updatable = false)
    private String loginName;

    // 密码
    @ApiField(value = "登录密码", desc = "使用MD5加密")
    @Column(nullable = false, length = 40, updatable = false)
    private String password;

    @ApiField(value = "姓名的拼音（全拼）")
    @Column(length = 40)
    private String pinyin;

    @ApiField(value = "性别，业务参数，来自BP_SEX")
    @Column(length = 40)
    private String sex;

    @ApiField(value = "电话号码")
    @Column(length = 20)
    private String phone;

    @ApiField(value = "手机号码")
    @Column(length = 20)
    private String mobile;

    @ApiField(value = "邮箱")
    @Column(length = 100)
    private String email;

    @ApiField(value = "头像")
    @Column(length = 40)
    private String icon;

    @ApiField(value = "账号状态", desc = "0、正常；1、锁定；2、离职")
    @Column
    private Integer locked;

    @ApiField(value = "所属组织机构ID")
    @Column(length = 40)
    private String orgId;
    @ApiField(value = "所属组织机构名称")
    @Column(length = 40)
    private String orgName;

    @ApiField(value = "岗位", desc = "来自业务参数:BP_DUTY")
    @Column(length = 40)
    private String duty;

    @ApiField(value = "角色ID", desc = "多个值使用逗号进行分隔")
    @Transient
    private String roles;

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String businessId() {
        return getId();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

}
