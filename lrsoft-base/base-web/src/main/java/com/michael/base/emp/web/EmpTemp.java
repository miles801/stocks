package com.michael.base.emp.web;

import com.michael.docs.annotations.ApiField;

/**
 * @author Michael
 */
public class EmpTemp {
    @ApiField(value = "登录名", required = true)
    private String loginName;

    @ApiField(value = "密码", required = true, desc = "MD5加密")
    private String password;

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
}
