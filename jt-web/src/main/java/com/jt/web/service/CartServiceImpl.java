package com.jt.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private HttpClientService httpClientService;

    private static final Logger logger = Logger.getLogger(CartService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SysResult insertCart(Cart cart) {
        String url = "http://cart.jt.com/cart/save";
        Map<String, String> cartMap = new HashMap<String, String>();
        cartMap.put("userId", cart.getUserId() + "");
        cartMap.put("itemId", cart.getItemId() + "");
        cartMap.put("itemTitle", cart.getItemTitle());
        cartMap.put("itemImage", cart.getItemImage()); //只保留一张图片
        cartMap.put("itemPrice", cart.getItemPrice() + "");
        cartMap.put("num", cart.getNum() + "");
        try {
            String sysResultJSON = httpClientService.doPost(url, cartMap);
            SysResult sysResult = objectMapper.readValue(sysResultJSON, SysResult.class);
            return sysResult;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return SysResult.build(201, "新增购物车失败");
        }


    }

    @Override
    public void deleteCart(Long userId, Long itemId) {
        String url = "http://cart.jt.com/cart/delete/" + userId + "/" + itemId;
        try {
            String sysResultJSON = httpClientService.doGet(url);
            objectMapper.readValue(sysResultJSON, SysResult.class);

            //保存数据 后期使用
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());

        }
    }

    @Override
    public SysResult updateCartNum(Long userId, Long itemId, Integer num) {
        //定义url
        try {
            String url = "http://cart.jt.com/cart/update/num/" + userId + "/" + itemId + "/" + num;
            String sysResultJSON = httpClientService.doGet(url);
            SysResult sysResult = objectMapper.readValue(sysResultJSON, SysResult.class);
            return sysResult;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return SysResult.build(201, "商品数量修改失败");
        }
    }

    @Override
    public List<Cart> findCartListByUserId(Long userId) {

        //定义url
        String url = "http://cart.jt.com/cart/query/" + userId;
        try {
            String sysResultJSOn = httpClientService.doGet(url);

            JsonNode jsonNode = objectMapper.readTree(sysResultJSOn);
            //返回的是JSON串 [{},{},{}] 获取JSON串中的数据
            String data = jsonNode.get("data").toString();
            //将JSON数据转化为Cart数组
            Cart[] carts = objectMapper.readValue(data, Cart[].class);
            //将对象数组转换为list集合
            List<Cart> cartList = Arrays.asList(carts);
            return cartList;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }


        return null;
    }
}
