package com.michael.utils;

/**
 * @author Michael
 */
public class NullUtils {

    /**
     * 判断对象是否为空，如果为空，则返回默认值，类似于
     * <pre>
     *     if(o==null){
     *         o= xxx
     *     }
     * </pre>
     *
     * @param o            要被检验是否为空的对象
     * @param defaultValue 默认值
     * @param <T>          对象的类型
     * @return 结果值
     */
    public static <T> T defaultValue(T o, T defaultValue) {
        if (o == null) {
            return defaultValue;
        }
        return o;
    }

}
