package com.michael.core.exception;

/**
 * Created by miles on 13-12-3.
 * 当参数为空时，抛出该异常（指定参数名称）
 */
public class NullParamException extends RuntimeException {
    private String param;

    public NullParamException() {
    }

    public NullParamException(String param) {
        super("parameter should not be null:" + param);
        this.param = param;
    }

    public NullParamException(String message, Throwable cause) {
        super("parameter should not be null:" + message, cause);
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}
