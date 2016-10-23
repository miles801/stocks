package com.michael.base.attachment;

import com.michael.base.attachment.vo.AttachmentVo;

/**
 * @author Michael
 */
public class AttachmentContext {
    private static ThreadLocal<AttachmentVo> _ = new ThreadLocal<AttachmentVo>();

    public static void set(AttachmentVo attachment) {
        _.set(attachment);
    }

    public static void clear() {
        _.remove();
    }

    public static AttachmentVo get() {
        return _.get();
    }
}
