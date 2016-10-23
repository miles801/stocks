package com.michael.utils.number;

import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author Michael
 */
public class IntegerUtils {

    /**
     * 多个Integer相加，如果为空则跳过
     */
    public static int add(Integer... numbers) {
        int i = 0;
        for (Integer n : numbers) {
            if (n != null) {
                i += n;
            }
        }
        return i;
    }

    /**
     * 两个数相乘，如果任意一个为空，则返回0
     */
    public static int multi(Integer a, Integer b) {
        if (a == null || b == null) {
            return 0;
        }
        return a * b;
    }

    /**
     * 返回一组整形数字中最大的那个数字。
     * 如果列表为空或则返回0
     *
     * @param values 要比较的数字集合
     * @return 最大值
     */
    public static int max(Integer... values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        int max = 0;
        for (Integer value : values) {
            if (value == null) {
                continue;
            }
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    /**
     * 返回一组整形数字中最小的那个数字。
     * 如果列表为空或则返回0
     *
     * @param values 要比较的数字集合
     * @return 最小值
     */
    public static int min(Integer... values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        int min = 0;
        for (Integer value : values) {
            if (value == null) {
                continue;
            }
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    /**
     * 将Long强转成Integer
     * 如果o为null，则返回0
     * 如果超出Integer的范围，会抛出异常
     *
     * @param o long型数字
     * @return 转换后的整数
     */
    public static Integer forceConvert(Long o) {
        if (o == null) {
            return 0;
        }
        return Integer.parseInt(o.toString());
    }

    /**
     * 判断前者是否比后者大
     * 如果为空，则视为0
     *
     * @param a
     * @param b
     * @return true，前者比后者大
     */
    public static boolean isBigger(Integer a, Integer b) {
        if (a == null) {
            a = 0;
        }
        if (b == null) {
            b = 0;
        }
        return a > b;
    }

    /**
     * 判断2个对象是否相当，如果为null，则转换为0
     *
     * @return true 相等
     */
    public static boolean nullEqual(Integer a, Integer b) {
        if (a == null) {
            a = 0;
        }
        if (b == null) {
            b = 0;
        }
        return Objects.equals(a, b);
    }

    /**
     * 将Long转成Integer，如果超出范围会抛出异常
     *
     * @param num 要转换的数字
     * @return 新的值
     */
    public static Integer parse(Long num) {
        if (num == null) {
            return null;
        }
        Assert.isTrue(num < Integer.MAX_VALUE, "值转换失败!超出最大范围!");
        return Integer.parseInt(num.toString());
    }

    /**
     * 将Long转成int，如果超出范围会抛出异常
     * 如果Long为null，则返回默认值
     *
     * @param num          要转换的数字
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static int parse(Long num, int defaultValue) {
        if (num == null) {
            return defaultValue;
        }
        Assert.isTrue(num < Integer.MAX_VALUE, "值转换失败!超出最大范围!");
        return Integer.parseInt(num.toString());
    }

    /**
     * 获取一个数字分批次后的批次数
     *
     * @param total 总数量
     * @param batch 每一批的数量
     * @return 批数
     */
    public static long times(long total, int batch) {
        Assert.isTrue(batch != 0, "除数不能为0!");
        long times = total / batch;
        if (total % batch > 0) {
            times++;
        }
        return times;
    }
}
