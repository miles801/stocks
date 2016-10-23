package com.michael.utils.collection;

import java.util.Collection;

/**
 * 集合相关的工具类
 *
 * @author Michael
 */
public class CollectionUtils {

    /**
     * 判断集合是否为空，null和empty都会返回true
     *
     * @param collection 集合对象
     * @return null，empty--> true，其他false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断一个集合是否不为空
     *
     * @param collection 集合对象
     * @return 与isEmpty相反
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}
