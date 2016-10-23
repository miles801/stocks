package com.michael.base.attachment.exception;

/**
 * @author miles
 * @datetime 2014/5/12 13:35
 */
public class AttachmentException extends RuntimeException {
    public AttachmentException() {
        super();
    }

    public AttachmentException(String message) {
        super(message);
    }

    public AttachmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttachmentException(Throwable cause) {
        super(cause);
    }
}
