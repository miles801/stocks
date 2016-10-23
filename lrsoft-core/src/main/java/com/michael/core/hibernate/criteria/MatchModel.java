package com.michael.core.hibernate.criteria;

/**
 * 条件匹配模式，默认是=
 *
 * @author Michael
 */
public enum MatchModel {
    /**
     * 等于（默认）
     */
    EQ,
    LIKE,
    NE,
    /**
     * 如果是IN，则值必须是Collection类型
     */
    IN,
    NOT,
    GT,
    LT,
    /**
     * column >= realValue
     * 例如：大于开始时间
     * startDate < new Date()
     */
    GE,
    /**
     * column < realValue
     */
    LE,
    NOT_IN,
    /**
     * 如果值为true，则使用isNull
     * 如果值为false，则使用isNotNull
     */
    NULL,
    /**
     * 与NULL相反
     */
    NOT_NULL,
    EMPTY
}
