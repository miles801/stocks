package com.michael.base.log.domain;

import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 登录日志
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_login_log")
public class LoginLog extends CommonDomain {
    @ApiField(value = "登录IP")
    @NotNull
    @Column(name = "ip", nullable = false, length = 20)
    private String ip;

    @ApiField(value = "登录时间")
    @NotNull
    @Column(name = "loginTime", nullable = false)
    private Date loginTime;

    @ApiField(value = "退出时间")
    @Column(name = "logoutTime")
    private Date logoutTime;

    @ApiField("在线时长")
    @Column
    private Long duration;

    @ApiField(value = "退出方式", desc = "参数:LOGOUT_TYPE")
    @Column(name = "type", length = 20)
    private String type;

    @ApiField(value = "备注")
    @Column(name = "description", length = 1000)
    private String description;

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Date getLogoutTime() {
        return this.logoutTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }


}
