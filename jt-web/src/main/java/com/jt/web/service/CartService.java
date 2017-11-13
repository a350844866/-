package com.jt.web.service;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;

import java.util.List;

public interface CartService {
    List<Cart> findCartListByUserId(Long userId);

    //根据userId和ItemId修改商品的数量
    SysResult updateCartNum(Long userId, Long itemId, Integer num);

    //根据userId和itemId删除购物车信息
    void deleteCart(Long userId, Long itemId);

    //新增购物车信息
    SysResult insertCart(Cart cart);
}
