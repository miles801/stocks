package com.michael.core.security;

/**
 * 登录信息常量
 *
 * @author miles
 * @datetime 13-12-15 下午11:47
 */
public interface LoginInfo {
    /**
     * 是否已经登录
     */
    String HAS_LOGIN = "hasLogin";

    /**
     * 员工id
     */
    String EMPLOYEE = "employeeId";

    /**
     * 员工名称
     */
    String EMPLOYEE_NAME = "employeeName";

    /**
     * 登录用户名
     */
    String USERNAME = "username";

    /**
     * 登录时间
     */
    String LOGIN_DATETIME = "loginDatetime";
    /**
     * 直属机构
     */
    String ORG = "orgId";
    /**
     * 直属机构名称
     */
    String ORG_NAME = "orgName";
}
