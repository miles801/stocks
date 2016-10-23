package com.michael.core.hibernate.filter;

/**
 * 过滤器字段的类型
 * 即：实体类中用于数据过滤的字段对应的是那种过滤类型
 *
 * @author Michael
 */
public enum FilterFieldType {
    /**
     * 组织机构
     */
    ORG,
    /**
     * 岗位
     */
    POSITION,
    /**
     * 员工
     */
    EMPLOYEE
    /**
     * 用户
     */
//    , USER
    /**
     * 系统
     */
    , PARAM
}
