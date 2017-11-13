package com.jt.cart.service;

import com.jt.cart.pojo.Cart;
import com.jt.common.vo.SysResult;

import java.util.List;

public interface CartService {
    List<Cart> findCartListByUserId(Long userId);

    //根据userId和商品id修改数量
    SysResult uodateCartNum(Long userId, Long itemId, Integer num);

    //根据userId和itemId删除购物车数据
    void deleteCart(Long userId, Long itemId);

    //新增购物车
    void saveCart(Cart cart);
}
