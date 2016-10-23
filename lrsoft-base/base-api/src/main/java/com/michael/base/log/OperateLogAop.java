package com.michael.base.log;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 操作日志的AOP
 *
 * @author Michael
 */
//@Aspect
//@Component
public class OperateLogAop {
    private Logger logger = Logger.getLogger(OperateLogAop.class);

    @Before(value = "execution(* *..dao.*.*(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        LogInfo log = method.getAnnotation(LogInfo.class);
        if (log != null) {
            String type = log.type().toString();
            String describe = log.describe();
        }
    }


}
