package com.michael.docs.annotations;

import java.lang.annotation.*;

/**
 * 标识接口，用于描述该类下的某些方法将会被启用
 *
 * @author Michael
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {
    /**
     * 描述信息
     */
    String value() default "";

    String desc() default "";

}
