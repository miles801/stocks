package com.michael.base.attachment.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author miles
 * @datetime 2014/5/9 12:46
 */
public class ImageToBase64 {
    public static void main(String[] args) throws IOException {
        InputStream input = ImageToBase64.class.getClassLoader().getResourceAsStream("demo.jpg");
        StringBuffer content = new StringBuffer();
        int bytesRead = 0;
        int chunkSize = 1000 * 1000; //1MB
        byte[] chunk = new byte[chunkSize];
        int i = 0;
        while ((bytesRead = input.read(chunk)) > 0) {
            i++;
            byte[] ba = new byte[bytesRead];
            System.arraycopy(chunk, 0, ba, 0, bytesRead);
            String encStr = new String(Base64.encodeBase64(ba, true));
            content.append(encStr);
        }
        System.out.println(content.toString());
        System.out.println("文件被切割了[" + i + "]次!");
    }
}
