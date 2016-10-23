package com.michael.utils.beans;

import com.michael.utils.gson.GsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author miles
 * @datetime 2014/7/6 18:46
 */
public class BeanCopyUtils {

    private static Logger logger = Logger.getLogger(BeanCopyUtils.class);

    /**
     * 拷贝源对象的所有属性到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(final Object source,
                                      final Object target) {
        PropertyDescriptor[] src = BeanUtils.getPropertyDescriptors(source.getClass());
        List<String> properties = new ArrayList<String>();
        for (PropertyDescriptor propertyDescriptor : src) {
            properties.add(propertyDescriptor.getName());
        }
        copyProperties(source, target, properties);
    }

    /**
     * 将属性从源对象拷贝到目标对象
     *
     * @param source            源对象
     * @param target            目标对象
     * @param includeProperties 指定拷贝的属性名称
     */
    public static void copyProperties(final Object source,
                                      final Object target,
                                      String[] includeProperties) {
        List<String> properties = includeProperties != null ? Arrays.asList(includeProperties) : null;
        copyProperties(source, target, properties);
    }


    /**
     * 判断一个对象是否为空，如果对象的属性也没有值，那么也视为空
     *
     * @return true：空对象
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object.toString().equals("")) {
            return true;
        }
        String json = GsonUtils.toJson(object);
        if ("{}".equals(json) || "[]".equals(json) || json.matches("\\{(\\w+:(null|\"\"))+\\}")) {
            return true;
        }
        return false;
    }

    public static void copyProperties(
            final Object source,
            final Object target,
            final Iterable<String> properties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper trg = new BeanWrapperImpl(target);

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Assert.notNull(properties, "Properties must not be null");

        for (final String propertyName : properties) {
            if ("class".contains(propertyName)) {//排除class属性
                continue;
            }
            try {
                trg.setPropertyValue(
                        propertyName,
                        src.getPropertyValue(propertyName)
                );
            } catch (Exception e) {
//                logger.info(src.getClass().getName() + "." + propertyName + "在" + target.getClass().getName() + "中不存在!");
            }
        }
    }

    /**
     * 拷贝属性，并排除指定名称的属性
     *
     * @param source   源对象
     * @param target   目标对象
     * @param excluded 要被排除的属性
     */

    public static void copyPropertiesExclude(final Object source,
                                             final Object target,
                                             final String[] excluded) {
        if (excluded == null || excluded.length < 1) {
            copyProperties(source, target);
            return;
        }
        PropertyDescriptor[] src = BeanUtils.getPropertyDescriptors(source.getClass());
        List<String> properties = new ArrayList<String>();
        for (PropertyDescriptor propertyDescriptor : src) {
            String name = propertyDescriptor.getName();
            if (org.apache.commons.lang.ArrayUtils.contains(excluded, name)) {
                continue;
            }
            properties.add(propertyDescriptor.getName());
        }
        copyProperties(source, target, properties);
    }
}
