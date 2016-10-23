package com.michael.core.exception;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityNotFoundException;

/**
 * 方法参数的工具类
 *
 * @author miles
 * @datetime 2014/7/5 10:33
 */
public class Argument {
    /**
     * 判断字符串是否为空，如果为空则抛出IllegalArgumentException
     *
     * @param obj     要判断的字符串
     * @param message 异常信息
     */
    public static void isEmpty(String obj, String message) {
        if (StringUtils.isEmpty(obj)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断对象是否为空，如果为空，则抛出IllegalArgumentException
     *
     * @param obj     要被判断的对象
     * @param message 异常信息
     */
    public static void isNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断对象是否为空，如果为空，则抛出EntityNotFoundException
     *
     * @param obj 实体对象
     * @param id  id
     */
    public static void entityNotFound(Object obj, String id) {
        if (obj == null) {
            throw new EntityNotFoundException(id);
        }
    }
}
