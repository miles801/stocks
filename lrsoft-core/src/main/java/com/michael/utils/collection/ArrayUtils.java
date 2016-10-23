package com.michael.utils.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author miles
 * @datetime 14-2-10 下午11:41
 */
public class ArrayUtils {
    /**
     * 将一个string字符串转换成整形的数组，其中以任意的非数字符号为分隔符
     *
     * @param str
     * @return
     */
    public static Integer[] string2IntArray(String str) {
        if (str == null || "".equals(str.trim())) {
            throw new RuntimeException("要被转换成数组的字符串不能为空!");
        }
        String strArr[] = str.split("\\D+");
        Integer data[] = new Integer[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            data[i] = Integer.parseInt(strArr[i]);
        }
        return data;
    }

    public static String[] listToArray(List<String> data) {
        if (data == null || data.size() < 1) return null;
        int length = data.size();
        String[] t = new String[length];
        for (int i = 0; i < data.size(); i++) {
            t[i] = data.get(i);
        }
        return t;
    }

    public static String[] setToArray(Set<String> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        String[] t = new String[data.size()];
        data.toArray(t);
        return t;
    }

    /**
     * 将Int集合转成int数组
     */
    public static Integer[] listToIntArray(List<Integer> data) {
        if (data == null || data.size() < 1) return null;
        int length = data.size();
        Integer[] t = new Integer[length];
        for (int i = 0; i < data.size(); i++) {
            t[i] = data.get(i);
        }
        return t;
    }

    /**
     * 将数组转换成List（ArrayList）
     */
    public static <T> List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList<T>();
        if (array == null || array.length == 0) {
            return list;
        }
        Collections.addAll(list, array);
        return list;
    }
}
