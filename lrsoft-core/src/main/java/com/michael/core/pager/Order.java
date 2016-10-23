package com.michael.core.pager;

/**
 * 排序对象
 */
public class Order {
    private String name;
    private boolean reverse;

    public Order() {
    }

    public Order(String name, boolean reverse) {
        this.name = name;
        this.reverse = reverse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
