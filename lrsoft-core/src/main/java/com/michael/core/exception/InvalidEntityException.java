package com.michael.core.exception;

/**
 * Created by miles on 13-11-21.
 * 无效的实体类！
 */
public class InvalidEntityException extends RuntimeException {

    public InvalidEntityException() {
    }

    public InvalidEntityException(String message) {
        super("Invalid entity:" + message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super("Invalid entity:" + message, cause);
    }
}
