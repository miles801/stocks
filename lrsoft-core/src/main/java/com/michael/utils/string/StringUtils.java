package com.michael.utils.string;

import com.michael.utils.bool.BooleanUtils;
import com.michael.utils.date.DateUtils;
import com.michael.utils.gson.GsonUtils;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Date;

/**
 * @author miles
 * @datetime 2014/4/16 16:55
 */
public class StringUtils {
    /**
     * 判断一个字符串是否为空或者空字符串
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断指定字符串集合中是否有任何一个是空的
     *
     * @return true:至少有一个是空的，false：无空的
     */
    public static boolean hasEmpty(String... str) {
        if (str == null || str.length == 0) {
            return true;
        }
        for (String foo : str) {
            if (isEmpty(foo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找指定的字符串是否在指定的数组中
     *
     * @param src    需要查找的字符串
     * @param arrays 要匹配的结果
     * @return true 在，false不在
     */
    public static boolean include(String src, String... arrays) {
        if (arrays == null || arrays.length == 0) {
            return false;
        }
        for (String arr : arrays) {
            boolean isEq = equals(src, arr);
            if (isEq) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个字符串是否相等（简化了对null的判断）
     *
     * @param str1 字符串1
     * @param str2 字符串1
     * @return true相等，false不相等
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        return str1 != null && str2 != null && str1.equals(str2);
    }

    /**
     * 判断两个字符串是否不等（是equals的取反）
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true不等，false相等
     */
    public static boolean notEquals(String str1, String str2) {
        return !equals(str1, str2);
    }

    /**
     * 字节转字符串
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        return new String(tempArr);
    }

    /**
     * 将字节数组转换为十六进制字符串
     */
    public static String byteToStr(byte[] byteArray) {
        StringBuilder buffer = new StringBuilder();
        for (byte aByteArray : byteArray) {
            buffer.append(byteToHexStr(aByteArray));
        }
        return buffer.toString();
    }


    /**
     * 将一个字符串使用utf-8进行两次解码
     */
    public static String decodeByUTF8(String str) {
        if (isEmpty(str)) return str;
        try {
            return URLDecoder.decode(URLDecoder.decode(str, "utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("无法解析的字符串或错误的编码!" + str);
    }

    /**
     * 将一个字符串使用iso-8859-1解码，然后使用utf-8编码
     */
    public static String encodeByUTF8(String str) {
        if (isEmpty(str)) return null;
        try {
            return new String(str.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("无法解析的字符串或错误的编码!" + str);
    }

    /**
     * 获得字符串的长度
     * 字母、英文标点一个长度
     * 汉字、中文标点、非法字符都认为2个字符
     */
    public static int getStringLength(String str) {
        int length = 0;
        byte[] bytes = null;
        for (char cn : str.toCharArray()) {
            try {
                bytes = (String.valueOf(cn)).getBytes("GBK");
            } catch (UnsupportedEncodingException ex) {
            }

            if (bytes == null || bytes.length > 2 || bytes.length <= 0) { //错误
                length += 2;
            } else if (bytes.length == 1) { //英文字符
                length++;
            } else if (bytes.length == 2) { //中文字符
                length += 2;
            }
        }
        return length;
    }

    /**
     * 将字符串数组按照指定的分隔符组合成一个新的字符串
     *
     * @param value 字符串数组
     * @param split 分隔符
     * @return 以分隔符分隔的一个单行字符串
     */
    public static String join(String[] value, String split) {
        if (value == null || value.length < 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String v : value) {
            builder.append(split).append(v);
        }
        return builder.substring(1);
    }

    /**
     * 将字符串集合按照指定的分隔符组合成一个新的字符串
     *
     * @param value 字符串集合
     * @param split 分隔符
     * @return 以分隔符分隔的一个单行字符串
     */
    public static String join(Collection<String> value, String split) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String v : value) {
            builder.append(split).append(v);
        }
        return builder.substring(1);
    }

    /**
     * 将字符串转成指定类型的对象
     * 如果字符串为空，则直接返回null
     * <p> 简单类型：int、Integer、double、Double、float、Float、Byte、byte、Boolean、boolean、java.utl.Date</p>
     * <p>复杂类型：利用json尝试进行转换</p>
     *
     * @param str 要转换的字符串
     * @param <T> 基本类型 & 复杂类型
     * @return 目标类型对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        Assert.notNull(clazz, "将字符串转成指定类型的对象失败:目标类型不能为空!");
        Object o = null;
        try {
            if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(int.class)) {
                o = Integer.parseInt(str);
            } else if (clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(double.class)) {
                o = Double.parseDouble(str);
            } else if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class)) {
                o = BooleanUtils.parse(str);
            } else if (clazz.isAssignableFrom(Date.class)) {
                o = DateUtils.parse(str);
            } else if (clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(short.class)) {
                o = Float.parseFloat(str);
            } else if (clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(short.class)) {
                o = Short.parseShort(str);
            } else if (clazz.isAssignableFrom(Byte.class) || clazz.isAssignableFrom(byte.class)) {
                o = Byte.parseByte(str);
            } else {
                o = GsonUtils.fromJson(str, clazz);
            }
        } catch (Exception e) {
            Assert.isTrue(false, String.format("将字符串[%s]转换成[%s]类型失败!", str, clazz.getName()));
        }
        return (T) o;
    }

}
