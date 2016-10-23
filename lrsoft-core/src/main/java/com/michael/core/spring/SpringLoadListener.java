package com.michael.core.spring;

import org.springframework.beans.factory.BeanFactory;

/**
 * Spring启动之后的事件
 *
 * @author Michael
 */
public interface SpringLoadListener {

    void execute(BeanFactory beanFactory);
}
