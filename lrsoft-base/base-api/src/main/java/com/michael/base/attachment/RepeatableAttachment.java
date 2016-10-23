package com.michael.base.attachment;

/**
 * 业务id不唯一时，可以通过业务的类来进行联合区分
 *
 * @author miles
 * @datetime 2014/5/7 12:52
 */
public interface RepeatableAttachment {
    Class<?> getBusinessClass();
}
