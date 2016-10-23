package com.michael.docs.annotations;

import java.lang.annotation.*;

/**
 * @author Michael
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiField {
    String value() default "";

    Class<?> relate() default Void.class;

    boolean required() default false;

    int length() default 0;

    String desc() default "";

    String type() default "string";
}
