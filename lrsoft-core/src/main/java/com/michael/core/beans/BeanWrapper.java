package com.michael.core.beans;

/**
 * 转换器：将源对象转换成目标对象
 * SRC 源对象：实体类
 * TARGET 目标对象
 * 建议使用BeanWrapBuilder代替，特殊转换实现BeanWrapCallback接口
 */
public interface BeanWrapper<SRC, TARGET> {
    /**
     * 将一个实体源对象转成目标对象
     *
     * @param src 源对象
     * @return 目标对象
     */
    TARGET wrap(SRC src);
}
