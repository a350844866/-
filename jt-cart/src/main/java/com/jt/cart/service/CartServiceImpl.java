package com.jt.cart.service;

import com.jt.cart.mapper.CartMapper;
import com.jt.cart.pojo.Cart;
import com.jt.common.service.BaseService;
import com.jt.common.vo.SysResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl extends BaseService<Cart> implements CartService {
    @Autowired
    private CartMapper cartMapper;

    private static final Logger logger = Logger.getLogger(CartServiceImpl.class);


    /**
     * 逻辑思维错误:
     * 参数列表中的cart是远程方法传递的数据其中没有主键信息id
     * 如果以该数据进行入库操作,可能会出现重复数据
     * 应该根据数据库中查出来的对象会准
     */

    @Override
    public void saveCart(Cart cart) {
        /**
         * 1.如果数据库有该商品信息应该做的是商品数量的叠加,而不是再次新增入库
         * 2.应该根据userId和itemId查询是否还有该数据
         *
         */
        Cart cartDB = new Cart();
        cartDB.setUserId(cart.getUserId());
        cartDB.setItemId(cart.getItemId());

        Cart findCart = super.queryByWhere(cartDB);
        if (findCart != null) {
            //证明数据含有该数据 应该做数量的更新操作
            int num = findCart.getNum() + cart.getNum();
            findCart.setNum(num);
            findCart.setUpdated(new Date());
            cartMapper.updateByPrimaryKey(findCart);
        } else {
            //表示数据库中没有改信息,直接插入
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cartMapper.insert(cart);
        }
    }

    public void deleteCart(Long userId, Long itemId) {
        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setUserId(userId);
        super.deleteByWhere(cart);

    }

    public SysResult uodateCartNum(Long userId, Long itemId, Integer num) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItemId(itemId);
        cart.setNum(num);
        cart.setUpdated(new Date());
        try {
            cartMapper.updateCartByUserIdAndItemId(cart);
            return SysResult.oK();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            SysResult.build(201, "商品数量修改失败");
        }
        return null;
    }

    public List<Cart> findCartListByUserId(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartMapper.select(cart);
    }
}
