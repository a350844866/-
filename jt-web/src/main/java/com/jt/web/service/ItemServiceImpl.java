package com.jt.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private HttpClientService httpClientService;

    @Autowired  //引入jedis缓存对象
    private JedisCluster jedisCluster;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = Logger.getLogger(ItemServiceImpl.class);

    /**
     * 问题:
     * 当前service中需要通过mapper查询数据库信息之后将数据回显
     * 当前的系统是前台系统,只负责数据的展现,不负责数据的查询.
     * 负责数据的查询应该是后台管理系统
     * 这样写的目的 : 解耦
     * 总结:需要通过当前的前台系统通过跨域请求查询后台系统的数据   !
     */
    //缓存操作之前
//    @Override
//    public Item findItemById(Long itemId) {
//        String url = "http://manage.jt.com/web/item/" + itemId;
//        try {
//            String itemJSON = httpClientService.doGet(url);
//            System.out.println(itemJSON);
//            //将JSON串转化为对象
//            Item item = objectMapper.readValue(itemJSON, Item.class);
//            return item;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.getMessage());
//        }
//        return null;
//    }
    @Override
    public Item findItemById(Long itemId) {
        String itemKey = "ITEM_" + itemId; //ITEM_100

        String redisJSON = jedisCluster.get(itemKey);


        try {
            if (StringUtils.isEmpty(redisJSON)) {
                String url = "http://manage.jt.com/web/item/" + itemId;
                String itemJSON = httpClientService.doGet(url);
                Item item = objectMapper.readValue(itemJSON, Item.class);
                jedisCluster.set(itemKey, itemJSON);
                return item;
            } else {
                Item item = objectMapper.readValue(redisJSON, Item.class);
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }


}
