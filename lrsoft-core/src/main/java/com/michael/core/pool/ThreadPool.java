package com.michael.core.pool;

import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author Michael
 */
public final class ThreadPool {
    private static ThreadPool ourInstance = new ThreadPool();

    public static ThreadPool getInstance() {
        return ourInstance;
    }

    private Logger logger = Logger.getLogger(ThreadPool.class);

    private ThreadPoolExecutor executor;

    private ThreadPool() {
        logger.info("初始化线程池....");
        executor = new ThreadPoolExecutor(100, 200, 50000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));
        logger.info("初始化线程池完成....");
    }

    /**
     * 执行一个线程
     *
     * @param runnable 线程
     */
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * 销毁线程池
     * 非系统模块，请勿调用！
     */
    public void shutdown() {
        executor.shutdownNow();
        logger.info("销毁线程池....当前线程数量:" + executor.getActiveCount());
    }

    protected ThreadPoolExecutor getExecutor() {
        return executor;
    }
}
