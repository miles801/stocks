package com.michael.core.beans;

import com.michael.core.beans.exceptions.BeanWrapException;
import com.michael.core.beans.exceptions.TargetClassNullException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 属性转换的工具类：
 * 提供将源对象转换成目标对象的相关简单工具方法
 * <p/>
 * 更高级的转换用法（转换指定的属性、排除指定的属性、回调等），可以使用VoWrapConfig实现
 */
public class BeanHelper {
    /**
     * 将一组源对象集合通过转化器转换为目标集合
     *
     * @param srcData  源对象
     * @param wrapper  目标对象
     * @param <SRC>    源对象类型
     * @param <TARGET> 目标对象类型
     */
    public static <SRC, TARGET> List<TARGET> wrapList(List<SRC> srcData, BeanWrapper<SRC, TARGET> wrapper) {
        List<TARGET> targets = new ArrayList<TARGET>();
        if (srcData == null) return targets;
        for (SRC o : srcData) {
            TARGET foo = wrapper.wrap(o);
            if (foo == null) continue;
            targets.add(foo);
        }
        return targets;
    }

    /**
     * 将源对象按照指定的转化器转换成目标对象
     *
     * @param srcData  源对象
     * @param wrapper  转化器
     * @param <SRC>    源对象类型
     * @param <TARGET> 目标对象类型
     * @return 目标对象
     */
    public static <SRC, TARGET> TARGET wrap(SRC srcData, BeanWrapper<SRC, TARGET> wrapper) {
        if (srcData == null) return null;
        if (wrapper == null) {
            throw new BeanWrapException("VO转换时转化器不能为空!");
        }
        return wrapper.wrap(srcData);
    }

    /**
     * 将源对象转换成指定类型的目标对象
     * 使用的是属性拷贝的方式，目标对象类型必须具有无参构造方法
     *
     * @param srcData     源对象
     * @param targetClass 目标对象类型
     * @param <SRC>       源对象类型
     * @param <TARGET>    目标对象类型
     * @return 目标对象
     */
    public static <SRC, TARGET> TARGET wrap(SRC srcData, Class<TARGET> targetClass) {
        if (srcData == null) return null;
        if (targetClass == null) {
            throw new TargetClassNullException("VO转换时没有指定目标类型!");
        }
        TARGET target = null;
        try {
            target = targetClass.newInstance();
            BeanUtils.copyProperties(srcData, target);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 将源对象换成目标对象，如果指定了回调函数，则在返回前进行回调处理
     *
     * @param srcData  源对象
     * @param callback 回调
     * @param <SRC>    源对象类型
     * @param <TARGET> 目标对象类型
     * @return 目标对象
     */
    public static <SRC, TARGET> TARGET wrap(SRC srcData, Class<TARGET> targetClass, BeanWrapCallback<SRC, TARGET> callback) {
        TARGET target = wrap(srcData, targetClass);
        if (callback != null) {
            callback.doCallback(srcData, target);
        }
        return target;
    }

}
