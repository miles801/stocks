package com.michael.base.attachment;

import java.io.InputStream;

/**
 * 附件的处理接口，获得流进行处理，一般不需要关闭，通过AttachmentProvider进行打开和关闭操作
 *
 * @author miles
 * @datetime 2014/7/18 12:12
 */
public interface AttachmentHandler {
    /**
     * 对流的具体处理
     *
     * @param stream 由AttachmentProvider注入的流
     */
    void handle(InputStream stream);
}
