package com.jt.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HttpClientService httpClientService;

    @Override
    public Order findOrderById(String id) {

        String url = "http://order.jt.com/order/query/" + id;
        try {
            String orderJSON = httpClientService.doGet(url);
            Order order = objectMapper.readValue(orderJSON, Order.class);
            return order;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public String saveOrder(Order order) throws Exception {

        String orderJSON = objectMapper.writeValueAsString(order);

        String url = "http://order.jt.com/order/create";
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderJSON", orderJSON);
        //orderJSON数据名称必须前后一致,否则传输失败
        String orderId = httpClientService.doPost(url, map, "utf-8");
        return orderId;
    }
}
