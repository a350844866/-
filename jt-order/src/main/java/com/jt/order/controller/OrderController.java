package com.jt.order.controller;

import com.jt.order.pojo.Order;
import com.jt.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/create")
    @ResponseBody
    public String saveOrder(String orderJSON) {
        String orderId = orderService.saveOrder(orderJSON);
        return orderId;
    }


    @RequestMapping("/query/{orderId}")
    @ResponseBody
    public Order findOrderById(@PathVariable String orderId) {
        Order order = orderService.findOrderById(orderId);
        return order;
    }
}
