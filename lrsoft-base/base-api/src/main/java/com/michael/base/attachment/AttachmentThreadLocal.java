package com.michael.base.attachment;

/**
 * 用于在同一个线程中保存附件的id集合
 *
 * @author miles
 * @datetime 2014/5/7 13:54
 */
public class AttachmentThreadLocal {
    /**
     * 附件ID
     */
    private static ThreadLocal<String[]> attachments = new ThreadLocal<String[]>();

    /**
     * 业务ID
     */
    private static ThreadLocal<String> bid = new ThreadLocal<String>();

    public static void set(String[] ids) {
        attachments.set(ids);
    }

    public static String[] get() {
        return attachments.get();
    }

    public static void clear() {
        attachments.remove();
        bid.remove();
    }

    public static void bindBusiness(String id) {
        bid.set(id);
    }

    public static String getBusiness() {
        return bid.get();
    }

    public static void removeBusiness() {
        bid.remove();
    }

}
