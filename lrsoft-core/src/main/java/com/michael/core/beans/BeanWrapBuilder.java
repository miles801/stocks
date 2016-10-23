package com.michael.core.beans;

import com.michael.core.beans.exceptions.BeanWrapException;
import com.michael.core.beans.exceptions.TargetClassNullException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * 属性转换的高级用法
 * 提供可配置的方式
 * Created by Michael on 2014/10/13.
 */
public class BeanWrapBuilder {
    private BeanWrapCallback callback;
    private Set<String> propertiesName;
    private boolean include = true;
    private boolean skip = false;

    public static BeanWrapBuilder newInstance() {
        return new BeanWrapBuilder();
    }

    public BeanWrapBuilder addProperties(String[] properties) {
        if (properties == null || properties.length < 1) return this;
        if (propertiesName == null) {
            propertiesName = new HashSet<String>();
        }
        Collections.addAll(propertiesName, properties);
        return this;
    }

    public BeanWrapBuilder exclude() {
        this.include = false;
        return this;
    }

    /**
     * 根据配置好的属性将指定对象转换成目标对象
     *
     * @param srcData     源对象
     * @param targetClass 目标对象类型
     * @param <SRC>       源对象类型
     * @param <TARGET>    目标对象类型
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    public <SRC, TARGET> TARGET wrap(SRC srcData, Class<TARGET> targetClass) {
        if (srcData == null) {
            return null;
        }
        if (targetClass == null) {
            throw new TargetClassNullException("VO转换时，没有指定目标对象类型!");
        }
        TARGET target = null;
        if (propertiesName == null || propertiesName.isEmpty()) {
            target = BeanHelper.wrap(srcData, targetClass);
        } else {
            try {
                target = targetClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (target == null) {
                throw new BeanWrapException("目标对象初始化失败,请检查该对象是否具有无参构造函数!");
            }

            final BeanWrapper src = new BeanWrapperImpl(srcData);
            final BeanWrapper trg = new BeanWrapperImpl(target);

            PropertyDescriptor[] srcProperties = BeanUtils.getPropertyDescriptors(srcData.getClass());
            for (PropertyDescriptor property : srcProperties) {
                String name = property.getName();
                if ("class".equals(name)) continue;
                boolean hasProperty = propertiesName.contains(name);
                if ((this.include && hasProperty) || (!this.include && !hasProperty)) {
                    try {
                        trg.setPropertyValue(name, src.getPropertyValue(name));
                    } catch (BeansException e) {
                        // 属性不存在
                    }
                }
            }
        }
        if (callback != null) {
            callback.doCallback(srcData, target);
            if (skip) {
                skip = false;
                return null;
            }
        }
        return target;
    }

    /**
     * 将一组源对象按照指定的规则转换成目标对象集合
     *
     * @param srcData     源对象集合
     * @param targetClass 目标对象的类型
     * @param <SRC>       源对象类型
     * @param <TARGET>    目标对象类型
     * @return 目标对象集合
     */
    public <SRC, TARGET> List<TARGET> wrapList(List<SRC> srcData, Class<TARGET> targetClass) {
        if (targetClass == null) {
            throw new TargetClassNullException("VO转换时目标类型不能为空!");
        }
        List<TARGET> targets = new ArrayList<TARGET>();
        if (srcData == null || srcData.isEmpty()) {
            return targets;
        }
        for (SRC src : srcData) {
            TARGET foo = wrap(src, targetClass);
            if (foo != null) {
                targets.add(foo);
            }
        }
        return targets;

    }

    public BeanWrapBuilder setCallback(BeanWrapCallback callback) {
        this.callback = callback;
        return this;
    }

    public void skip() {
        this.skip = true;
    }
}
