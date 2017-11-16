package com.jt.order.mq.service;

import com.jt.order.mapper.OrderMapper;
import com.jt.order.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;

//表示rabbitMQ的接收端
public class RabbitOrderService {

    @Autowired
    private OrderMapper orderMapper;

    //消费者接受的是生产者提供的数据
    //当前传递的是对象 所以必须序列化 否则不能传输
    public void saveOrder(Order order) {

        orderMapper.saveOrder(order);
        System.out.println("rabbitMQ消费者调用成功!!!");

    }
}
