package com.michael.core.exception;

/**
 * Created by miles on 13-12-4.
 * 参数无效时的异常
 */
public class InvalidParamException extends RuntimeException {
    private static String pref = "invalid parameter:";

    public InvalidParamException() {
    }

    public InvalidParamException(String message) {
        super(pref.concat(message));
    }

    public InvalidParamException(String message, Throwable cause) {
        super(pref.concat(message), cause);
    }
}
