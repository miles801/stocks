package com.michael.core.beans.exceptions;

/**
 * 属性转换时异常
 * Created by Michael on 2014/10/13.
 */
public class BeanWrapException extends RuntimeException {
    public BeanWrapException() {
        super();
    }

    public BeanWrapException(String message) {
        super(message);
    }

    public BeanWrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanWrapException(Throwable cause) {
        super(cause);
    }

}
