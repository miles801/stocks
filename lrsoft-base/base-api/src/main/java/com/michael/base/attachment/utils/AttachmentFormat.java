package com.michael.base.attachment.utils;

/**
 * @author miles
 * @datetime 2014/5/12 14:21
 */
public class AttachmentFormat {
    private static long BYTE = 1;
    private static long KB = BYTE * 1000;
    private static long MB = KB * 1000;
    private static long GB = MB * 1000;
    private static long TB = GB * 1000;

    /**
     * 格式化显示文件的大小
     *
     * @param size
     * @return
     */
    public static String format(long size) {
        if (size < KB) {
            return size + "B";
        }
        if (size >= KB && size < MB) {
            return (size / KB) + "KB";
        }
        if (size >= MB && size < GB) {
            return (size / MB) + "MB";
        }
        if (size >= GB && size < TB) {
            return (size / GB) + "GB";
        }
        return (size / TB) + "TB";
    }
}
