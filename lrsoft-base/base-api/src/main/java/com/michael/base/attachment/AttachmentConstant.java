package com.michael.base.attachment;

/**
 * @author miles
 * @datetime 2014/5/7 13:52
 */
public interface AttachmentConstant {
    /**
     * 用于往session中放置信息
     */
    String ATTACHMENT_INFO = "attachment_info";

    /**
     * 附件保存目录
     */
    String ATTACHMENT_ACTIVE = "attachment.active";

    // 过期时间（单位为小时）
    String ATTACHMENT_EXPIRED = "attachment.expired";

    /**
     * 附件查看地址
     */
    String ATTACHMENT_URL_VIEW = "attachment.url.view";

    /**
     * 图片附件的获取地址
     */
    String ATTACHMENT_URL_IMAGE = "attachment.url.image";

    /**
     * 附件的下载地址
     */
    String ATTACHMENT_URL_DOWNLOAD = "attachment.url.download";

}
