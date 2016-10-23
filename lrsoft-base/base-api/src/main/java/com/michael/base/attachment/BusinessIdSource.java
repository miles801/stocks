package com.michael.base.attachment;

/**
 * 业务ID来源
 *
 * @author Michael
 */
public enum BusinessIdSource {
    /**
     * 方法返回值
     */
    RETURN_VALUE,
    /**
     * 手动指定
     *
     * @see AttachmentThreadLocal#bindBusiness(String)
     */
    ASSIGN
}
