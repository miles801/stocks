package com.michael.core.pool;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池监控的定时器，用于定时打印当前的线程状况!
 *
 * @author Michael
 */
@Component
public class ThreadMonitor {

    private Logger logger = Logger.getLogger(ThreadMonitor.class);

    /**
     * 每20秒执行一次
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void execute() {
        ThreadPoolExecutor executor = ThreadPool.getInstance().getExecutor();
        logger.info(String.format("Thread Pool --> corePoolSize : %d, maxPoolSize : %d, activeCount : %d, taskCount : %d", executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getActiveCount(), executor.getTaskCount()));
    }
}
