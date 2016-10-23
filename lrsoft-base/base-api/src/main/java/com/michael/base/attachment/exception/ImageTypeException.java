package com.michael.base.attachment.exception;

/**
 * @author miles
 * @datetime 2014/5/12 13:38
 */
public class ImageTypeException extends RuntimeException {
    public ImageTypeException() {
        super();
    }

    public ImageTypeException(String message) {
        super(message);
    }

    public ImageTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageTypeException(Throwable cause) {
        super(cause);
    }
}
