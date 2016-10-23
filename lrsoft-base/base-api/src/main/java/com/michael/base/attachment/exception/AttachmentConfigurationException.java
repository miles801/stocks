package com.michael.base.attachment.exception;

/**
 * 附件配置相关的异常
 *
 * @author miles
 * @datetime 2014/5/12 14:00
 */
public class AttachmentConfigurationException extends AttachmentException {
    public AttachmentConfigurationException() {
        super();
    }

    public AttachmentConfigurationException(String message) {
        super(message);
    }

    public AttachmentConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttachmentConfigurationException(Throwable cause) {
        super(cause);
    }
}
