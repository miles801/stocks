package com.michael.base.attachment;

import java.lang.annotation.*;

/**
 * 在接口上使用该注解，表示该方法将会被拦截
 *
 * @author Michael
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface HasAttachment {
    BusinessIdSource bid() default BusinessIdSource.RETURN_VALUE;
}
