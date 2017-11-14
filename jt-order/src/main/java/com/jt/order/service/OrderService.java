package com.jt.order.service;

import com.jt.order.pojo.Order;

public interface OrderService {
    String saveOrder(String orderJSON);

    Order findOrderById(String orderId);
}
