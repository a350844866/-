package com.jt.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.pojo.Order;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order findOrderById(String orderId) {
        Order order = orderMapper.findOrderById(orderId);
        return order;
    }

    @Override
    public String saveOrder(String orderJSON) {


        //1.将JSON串进行格式转化,转化为Order对象
        try {
            Order order = objectMapper.readValue(orderJSON, Order.class);
            String orderId = order.getUserId() + "" + System.currentTimeMillis();
            order.setOrderId(orderId);
            order.setCreateTime(new Date());
            order.setUpdateTime(order.getCreateTime());
            order.getOrderShipping().setCreated(order.getCreateTime());
            order.getOrderShipping().setUpdated(order.getCreateTime());
            orderMapper.saveOrder(order);
            return orderId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }
}
