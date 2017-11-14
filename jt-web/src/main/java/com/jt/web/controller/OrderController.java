package com.jt.web.controller;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Order;
import com.jt.web.service.CartService;
import com.jt.web.service.OrderService;
import com.jt.web.util.UserThreadLocal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    public static final Logger logger = Logger.getLogger(OrderController.class);

    @RequestMapping("/create")
    public String createOrder(Model model) {

        Long userId = UserThreadLocal.getUser().getId();

        //转向时需要购物车的列表信息
        List<Cart> cartList = cartService.findCartListByUserId(userId);
        model.addAttribute("carts", cartList);
        return "order-cart";
    }


    @RequestMapping("/submit")
    @ResponseBody
    public SysResult saveOrder(Order order) {
        //获取userId
        Long userId = UserThreadLocal.getUser().getId();
        order.setUserId(userId);
        //返回订单编号
        try {
            String orderId = orderService.saveOrder(order);
            return SysResult.oK(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201, "订单新增失败");
        }
    }

    //订单成功后的页面跳转
    @RequestMapping("/success")
    public String findOrderById(String id, Model model) {
        //根据id
        Order order = orderService.findOrderById(id);
        model.addAttribute("order", order);
        return "success";
    }

}
