package com.michael.core.beans.exceptions;

/**
 * 目标类型为空的异常
 * Created by Michael on 2014/10/13.
 */
public class TargetClassNullException extends BeanWrapException {
    public TargetClassNullException() {
        super();
    }

    public TargetClassNullException(String message) {
        super(message);
    }

    public TargetClassNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public TargetClassNullException(Throwable cause) {
        super(cause);
    }

}
