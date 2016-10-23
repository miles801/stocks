package com.michael.docs.annotations;

import java.lang.annotation.*;

/**
 * 方法
 *
 * @author Michael
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperate {

    /**
     * 方法作用描述
     */
    String value() default "";

    /**
     * 请求方式，常用的值有：get/post/put/delete
     */
    String method() default "get";

    String desc() default "";

    /**
     * 请求时的数据类型
     */
    Class<?> request() default Void.class;

    /**
     * 响应类型
     */
    Class<?> response() default Void.class;

    /**
     * 请求时的类型
     * 可能的值为：
     * 1. application/json
     */
    String requestContentType() default "application/json";


}
