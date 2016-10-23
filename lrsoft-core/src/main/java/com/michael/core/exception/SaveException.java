package com.michael.core.exception;

/**
 * Created by miles on 13-12-3.
 * 保存时的异常
 */
public class SaveException extends RuntimeException {
    public SaveException() {
    }

    public SaveException(String message) {
        super("an exception caused while executing save:" + message);
    }

    public SaveException(String message, Throwable cause) {
        super("an exception caused while executing save:" + message, cause);
    }
}
