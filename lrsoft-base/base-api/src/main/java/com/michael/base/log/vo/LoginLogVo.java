package com.michael.base.log.vo;

import com.michael.base.log.domain.LoginLog;

/**
 * @author Michael
 */
public class LoginLogVo extends LoginLog {
    // 退出方式--参数名称
    private String typeName;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }
}
