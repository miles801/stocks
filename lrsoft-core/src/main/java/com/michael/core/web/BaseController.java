package com.michael.core.web;

import com.michael.utils.gson.GsonUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by miles on 13-11-15.
 */
public class BaseController {
    /**
     * 从request请求中获得指定参数名称的参数值
     * 如果没有指定名称的参数，或者参数的值为空，则返回null
     *
     * @param request
     * @param paramName
     * @return String/null
     */
    public String param(HttpServletRequest request, String paramName) {
        if (paramName == null || "".equals(paramName.trim())) {
            return null;
        }
        String param = request.getParameter(paramName);
        if (param == null || "".equals(param.trim())) {
            return null;
        }
        return param;
    }

    /**
     * 统一异常处理
     * 打印异常信息
     * 将简要异常和详细异常分别添加到simpleEx和exception中
     * 异常信息为倒叙，即从最后往前开可以追溯到异常的本源
     * 返回{error:true,message:'',data:'detail message'}
     */
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response, HttpServletRequest request) {
        StringBuilder detail = new StringBuilder(500);
        StringBuilder simple = new StringBuilder(100);
        StackTraceElement[] elements = e.getStackTrace();
        detail.append(e.toString());
        Throwable cause = e.getCause();
        while (cause != null && cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause != null) {
            if (cause.getMessage().startsWith("Data truncation: Data too long for column")) {
                simple.append("内容长度超过限制!");
            } else {
                simple.append(cause.getMessage());
            }
        } else {
            simple.append(e.getMessage());
        }
        for (StackTraceElement element : elements) {
            detail.append("\r\n\tat  ")
                    .append(element.getClassName()).append(".")// 类名称
                    .append(element.getMethodName()).append(": ")// 方法名称
                    .append(element.getLineNumber());// 行号
        }

        request.setAttribute("exception", detail.toString());
        request.setAttribute("simpleEx", simple.toString());
        e.printStackTrace();
        GsonUtils.printError(response, "500", simple.toString(), detail.toString());
    }
}
