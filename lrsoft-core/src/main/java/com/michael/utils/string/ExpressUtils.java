package com.michael.utils.string;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * 利用Spring的spel封装的表达式工具类，用于简单的数学运算，字符串求值等
 *
 * @author Michael
 */
public class ExpressUtils {

    /**
     * 简单的算术计算，返回double
     *
     * @param expStr 算术表达式
     */
    public static double calculateDouble(String expStr) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expStr);
        String result = exp.getValue().toString();
        return Double.parseDouble(result);
    }

    /**
     * 简单的算术运算，返回int
     *
     * @param expStr 算术表达式
     */
    public static int calculateInt(String expStr) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expStr);
        String result = exp.getValue().toString();
        return Integer.parseInt(result);
    }
}
