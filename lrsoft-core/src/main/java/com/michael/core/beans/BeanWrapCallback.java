package com.michael.core.beans;

/**
 * 属性转换后的回调接口
 * 用于转换后进行一些特殊处理的回调
 * Created by Michael on 2014/10/13.
 */
public interface BeanWrapCallback<SRC, TARGET> {

    void doCallback(SRC src, TARGET target);

}
