package com.michael.base.emp.web;

import com.michael.docs.annotations.ApiField;

/**
 * @author Michael
 */
public class PwdTemp {
    @ApiField(value = "新密码", required = true, desc = "使用MD5加密")
    private String newPwd;

    @ApiField(value = "原始密码", required = true, desc = "使用MD5加密")
    private String oldPwd;

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }
}
