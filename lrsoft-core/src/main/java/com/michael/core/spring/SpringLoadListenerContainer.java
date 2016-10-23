package com.michael.core.spring;

import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统启动后的要执行的操作
 *
 * @author Michael
 */
public final class SpringLoadListenerContainer {
    private static SpringLoadListenerContainer instance = new SpringLoadListenerContainer();

    private List<SpringLoadListener> listeners = new ArrayList<SpringLoadListener>();


    private SpringLoadListenerContainer() {
    }

    public static SpringLoadListenerContainer getInstance() {
        return instance;
    }


    public void setListeners(List<SpringLoadListener> listeners) {
        this.listeners = listeners;
    }

    public List<SpringLoadListener> getListeners() {
        return listeners;
    }

    public void add(SpringLoadListener listener) {
        listeners.add(listener);
    }

    public void execute(BeanFactory beanFactory) {
        for (SpringLoadListener listener : listeners) {
            listener.execute(beanFactory);
        }
    }
}
