package com.michael.base.attachment;

/**
 * 一个实体中有多个字段可以上传附件
 *
 * @author miles
 * @datetime 2014/5/7 12:48
 */
public interface MultiAttachment {
    /**
     * 附件所属业务的类型
     *
     * @return
     */
    String getBusinessType();
}
