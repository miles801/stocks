package com.michael.core;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * 系统的Bean容器，在spring启动后注入BeanFactory
 *
 * @author miles
 * @datetime 2014/4/17 2:05
 */
public class SystemContainer {
    private static SystemContainer container = new SystemContainer();
    private BeanFactory beanFactory;

    private SystemContainer() {
    }

    public static SystemContainer getInstance() {
        return container;
    }

    public static SystemContainer newInstance(BeanFactory beanFactory) {
        container.beanFactory = beanFactory;
        return container;
    }

    /**
     * 根据名称获得Bean对象
     *
     * @param beanName bean的名称
     */
    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 获取Bean，如果Bean不存在则返回null
     * 如果存在多个重名的Bean，则抛出异常
     *
     * @param clazz Bean类型
     * @param <T>   Bean的类类型
     * @return Bean对象
     */
    public <T> T getBean(Class<T> clazz) {
        try {
            return beanFactory.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            // 异常时不处理
        }
        return null;
    }


    /**
     * 如果一个接口有多个实现，则使用此方法获取bean
     *
     * @param clazz    接口
     * @param beanName 接口对应实现的bean的名称
     */
    public <T> T getBean(Class<T> clazz, String beanName) {
        return beanFactory.getBean(beanName, clazz);
    }

}
