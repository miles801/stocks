package com.michael.core.exception;

/**
 * Created by miles on 13-12-8.
 * 引用数据异常：通常发生在删除某条数据时，该数据被其他记录引用（外键关联）
 * 一般传递id作为构造方法的参数
 */
public class ReferencedException extends RuntimeException {
    private static String pref = "data can not be deleted ,because some records other referenced:";

    public ReferencedException(String message) {
        super(pref + message);
    }

    public ReferencedException(String message, Throwable cause) {
        super(pref + message, cause);
    }
}
