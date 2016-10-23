package com.michael.utils.bool;

/**
 * @author Michael
 */
public class BooleanUtils {
    /**
     * 将字符串转换成Boolean
     * 空字符串将会转成false
     * [y,yes,r,right,t,on,true,1,正确,是]将会被转换成true，其余的都会被转成false
     *
     * @param string 要解析的字符串
     */
    public static boolean parse(String string) {
        if (string == null || "".equals(string.trim())) {
            return false;
        }
        final String lowerCase = string.toLowerCase().trim();
        String[] arr = new String[]{"y", "yes", "r", "right", "t", "true", "是", "正确", "1"};
        for (String a : arr) {
            if (a.equals(lowerCase)) {
                return true;
            }
        }
        return false;
    }
}
