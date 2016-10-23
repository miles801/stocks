package com.michael.base.emp.vo;

import com.michael.base.emp.domain.Emp;
import com.michael.docs.annotations.ApiField;

/**
 * @author Michael
 */
public class EmpVo extends Emp {

    // 性别
    @ApiField(value = "性别（中文名称）")
    private String sexName;
    // 职务
    @ApiField(value = "职务（中文名称）")
    private String dutyName;
    // 状态
    @ApiField(value = "状态（中文名称）")
    private String lockedName;

    // 状态
    @ApiField(value = "岗位等级（中文名称）")
    private String levelName;

    private String token;

    @ApiField("角色ID，多个值使用逗号分隔")
    private String roleIds;
    @ApiField("角色名称，多个值使用逗号分隔")
    private String roleNames;

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getLockedName() {
        return lockedName;
    }

    public void setLockedName(String lockedName) {
        this.lockedName = lockedName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
