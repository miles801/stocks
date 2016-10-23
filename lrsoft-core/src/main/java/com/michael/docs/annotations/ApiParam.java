package com.michael.docs.annotations;

import java.lang.annotation.*;

/**
 * 参数
 *
 * @author Michael
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {
    /**
     * 参数的名称
     */
    String name();

    /**
     * 参数描述
     */
    String value() default "";

    /**
     * 是否必须
     */
    boolean required() default true;

    /**
     * 描述
     */
    String desc() default "";
}
