package com.jt.web.controller;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.service.CartService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    private static final Logger logger = Logger.getLogger(CartController.class);

    @RequestMapping("/show")
    public String toCart(Model model) {

        //自定义userId 后期修改
        Long userId = 2L;
        List<Cart> cartList = cartService.findCartListByUserId(userId);
        model.addAttribute("cartList", cartList);
        //表示转向购物车页面
        return "cart";
    }

    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public SysResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num) {
        /**
         * 1.说明:哪些操作需要添加try-catch
         * 如果需要对返回值进行进一步的操作,可能由于方法的原因造成程序异常终止,需要再次try-catch
         */
        try {
            Long userId = 2L;
            SysResult sysResult = cartService.updateCartNum(userId, itemId, num);
            return sysResult;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return SysResult.build(201, "修改商品数量失败");
        }
    }

    @RequestMapping("/delete/{itemId}")
    public String deleteCart(@PathVariable Long itemId) {
        Long userId = 2L;
        cartService.deleteCart(userId, itemId);

        //应该转向到购物车页面
        return "redirect:/cart/show.html";//采用伪静态的方式返回
    }

    @RequestMapping("/add/{itemId}")
    public String insertCart(@PathVariable Long itemId, Cart cart) {
        //TODO
        cart.setUserId(2L);
        cart.setItemId(itemId);

        cartService.insertCart(cart);

        return "redirect:/cart/show.html";//通过伪静态的方式提交
    }
}
