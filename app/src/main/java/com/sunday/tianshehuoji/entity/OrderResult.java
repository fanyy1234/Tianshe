package com.sunday.tianshehuoji.entity;

import com.sunday.tianshehuoji.entity.order.OrderItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class OrderResult {

    private Object size;
    private OrderDetail order;

    public Object getSize() {
        return size;
    }

    public void setSize(Object size) {
        this.size = size;
    }

    public OrderDetail getOrder() {
        return order;
    }

    public void setOrder(OrderDetail order) {
        this.order = order;
    }
}
