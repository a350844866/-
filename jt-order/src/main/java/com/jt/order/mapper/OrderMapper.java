package com.jt.order.mapper;


import com.jt.order.pojo.Order;

public interface OrderMapper {


    void saveOrder(Order order);

    Order findOrderById(String orderId);

    //表示将两天前的数据 修改状态
    void updateStatus();
}
