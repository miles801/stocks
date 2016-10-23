package com.michael.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Michael
 */
@Component
@Aspect
public class LogAop {
    private Logger logger = Logger.getLogger(LogAop.class);

    @Around(value = "execution(* *..service.impl.*.*(..)) || execution(* *..service.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        // 获得类名称
        String className = joinPoint.getTarget().getClass().getName();
        // 获得方法名称
        String methodName = joinPoint.getSignature().getName();
        Object obj = joinPoint.proceed();
        long end = System.currentTimeMillis();
        if (end - start > 50) {
            logger.warn("处理时间过长(" + (end - start) + "ms):" + className + "." + methodName);
        }
        return obj;
    }
}
