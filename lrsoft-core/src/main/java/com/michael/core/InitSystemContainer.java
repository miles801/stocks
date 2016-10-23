package com.michael.core;

import com.michael.core.spring.SpringLoadListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author Michael
 */
public class InitSystemContainer implements SpringLoadListener {
    private Logger logger = Logger.getLogger(InitSystemContainer.class);

    @Override
    public void execute(BeanFactory beanFactory) {
        logger.info("初始化SystemContainer...");
        SystemContainer.newInstance(beanFactory);
    }
}
