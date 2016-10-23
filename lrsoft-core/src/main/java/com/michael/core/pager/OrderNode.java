package com.michael.core.pager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderNode {

    private List<Order> orders;
    private Iterator<Order> iterator = null;

    public OrderNode() {
    }

    public OrderNode addOrder(Order order) {
        if (orders == null) {
            orders = new ArrayList<Order>();
        }
        orders.add(order);
        return this;
    }

    public boolean hasNext() {
        if (iterator == null) {
            iterator = orders.iterator();
        }
        return iterator.hasNext();
    }

    public Order next() {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
}
