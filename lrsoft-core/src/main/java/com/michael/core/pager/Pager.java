package com.michael.core.pager;


import org.springframework.util.Assert;

/**
 * 分页辅助类，提供起始位置和查询个数
 */
public class Pager {
    private static ThreadLocal<Integer> _start = new ThreadLocal<Integer>();
    private static ThreadLocal<Integer> _limit = new ThreadLocal<Integer>();
    private static ThreadLocal<OrderNode> _order = new ThreadLocal<OrderNode>();

    private static boolean valid = false;//是否有效，当设置start或者limit时，自动生效，移除时失效

    /**
     * 如果为空，则返回0
     */
    public static Integer getStart() {
        return _start.get();
    }

    public static void setStart(Integer start) {
        valid = true;
        _start.set(start);
    }

    public static void removeStart() {
        valid = false;
        _start.remove();
    }

    /**
     * 如果为空，则返回Integer的最大值
     */
    public static Integer getLimit() {
        return _limit.get();
    }

    public static void setLimit(Integer limit) {
        valid = true;
        _limit.set(limit);
    }

    public static void removeLimit() {
        valid = false;
        _limit.remove();
    }

    public static boolean isValid() {
        return valid;
    }

    public static OrderNode getOrder() {
        return _order.get();
    }

    public static void removeOrder() {
        Pager._order.remove();
    }

    /**
     * 默认为false
     */
    public static void addOrder(String propertyName, boolean reverse) {
        Assert.hasText(propertyName, "添加排序属性时,要排序的属性名称不能为空!");
        Order order = new Order(propertyName, reverse);
        OrderNode orderNode = getOrder();
        if (orderNode == null) {
            orderNode = new OrderNode();
            _order.set(orderNode);
        }
        orderNode.addOrder(order);
    }


    /**
     * 移除所有设置的项
     */
    public static void clear() {
        _order.remove();
        _start.remove();
        _limit.remove();
    }
}
