package com.michael.core.spring;

import com.michael.core.SystemContainer;
import com.michael.core.pool.ThreadPool;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 自定义Spring启动器
 * <p>
 * 在Spring启动之后可以通过实现com.michael.core.SpringLoadListener接口做一些额外的操作
 * </p>
 * <p>
 * 是Spring卸载时，可以通过实现com.michael.core.spring.SpringUnloadListener接口来做一些额外的操作，例如关闭连接（释放资源）、停止定时器等
 * </p>
 *
 * @author Michael
 */
public class SpringContextLoader extends ContextLoader implements ServletContextListener {
    private Logger logger = Logger.getLogger(SpringContextLoader.class);
    private ContextLoader contextLoader;
    private BeanFactory beanFactory;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("开始启动系统....");


        // 启动spring
        logger.info("启动Spring...");
        this.contextLoader = this;
        WebApplicationContext webContext = this.contextLoader.initWebApplicationContext(servletContextEvent.getServletContext());
        SystemContainer.newInstance(webContext);
        beanFactory = webContext;
        logger.info("Spring启动成功....");

        // 执行其他操作
        SpringLoadListenerContainer container = (SpringLoadListenerContainer) beanFactory.getBean("springLoadListenerContainer");
        if (container == null) {
            logger.warn(String.format("没有注册Spring启动后的事件服务[%s]", SpringLoadListenerContainer.class.getName()));
        } else {
            container.execute(webContext);
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("系统即将关闭，开始调用已注册的卸载程序....");

        // 关闭线程池
        ThreadPool.getInstance().shutdown();

        logger.info("准备停用spring定时器....");
        ScheduledAnnotationBeanPostProcessor processor = SystemContainer.getInstance().getBean(ScheduledAnnotationBeanPostProcessor.class);
        try {
            processor.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("停用spring定时器....OK");


        SpringUnloadListenerContainer container = SystemContainer.getInstance().getBean(SpringUnloadListenerContainer.class);
        container.execute(servletContextEvent.getServletContext());
        logger.info("系统即将关闭，开始调用已注册的卸载程序....OK");

    }
}
