package com.michael.base.attachment.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author miles
 * @datetime 2014/5/12 13:08
 */
public class ImageUtils {
    /**
     * 将图片输入流转换成base64字符串
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static String image2Base64(InputStream input) throws IOException {
        StringBuffer content = new StringBuffer();
        int bytesRead = 0;
        int chunkSize = 1000 * 1000; //1MB
        byte[] chunk = new byte[chunkSize];
        while ((bytesRead = input.read(chunk)) > 0) {
            byte[] ba = new byte[bytesRead];
            System.arraycopy(chunk, 0, ba, 0, bytesRead);
            String encStr = new String(Base64.encodeBase64(ba, true));
            content.append(encStr);
        }
        return content.toString();
    }
}
