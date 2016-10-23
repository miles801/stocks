package com.michael.core.exception;

/**
 * Created by miles on 13-12-3.
 * 更新时发生异常
 */
public class UpdateException extends RuntimeException {
    public UpdateException() {
    }

    public UpdateException(String message) {
        super("an exception caused while executing update:" + message);
    }

    public UpdateException(String message, Throwable cause) {
        super("an exception caused while executing update:" + message, cause);
    }
}
