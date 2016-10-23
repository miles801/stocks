package com.michael.base.log;

import java.lang.annotation.*;

/**
 * @author Michael
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LogInfo {

    com.michael.base.log.OperateType type() default com.michael.base.log.OperateType.ADD;

    String describe() default "";
}
