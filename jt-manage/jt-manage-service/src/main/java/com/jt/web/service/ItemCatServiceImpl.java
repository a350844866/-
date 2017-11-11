package com.jt.web.service;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisSentinelService;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;
    //引入缓存机制

//    @Autowired
//    private ShardRedisService redisService;

//    @Autowired
//    private RedisSentinelService redisService;

    @Autowired
    private JedisCluster redisService;

    private static final Logger LOGGER = Logger.getLogger(ItemServiceImpl.class);

    //将java数据和JSON串之间进行转化
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 1.当用户通过parentId查询子级数据时,应该先从缓存中获取数据
     * 2.如果获取的数据不为Null,需要将字符串String转化为ItemCat对象
     * 3.如果获得的数据为null,需要通过parentId从数据库查询对应的信息后得到List<ItemCat>对象
     * 4.需要将查询的数据转化为JSON串,ObjectMapper
     * 5.将数据写入到缓存中 返回用户结果
     */

    @Override
    public List<ItemCat> findItemCatList(Long parentId) {
        List<ItemCat> itemCatList;

        String itemCatKey = "ITEM_CAT_" + parentId; //ITEM_CAT_0
        String JSONData = redisService.get(itemCatKey);
        try {
            if (StringUtils.isEmpty(JSONData)) {
                ItemCat itemCat = new ItemCat();
                itemCat.setParentId(parentId);

                //sql select * from tb_item_cat where parent_id = XXX
                itemCatList = itemCatMapper.select(itemCat);

                String JSONRsult = objectMapper.writeValueAsString(itemCatList);
                redisService.set(itemCatKey, JSONRsult);
                return itemCatList;
            } else {
                //需要将返回后的JSON数据转化为具体对象
                //可以直接将对象转化为想要的格式
                //由于为了实现EasyUI回显为pojo对象中添加了text属性和state这些属性在转化时必然报错.
                //由于其中没有对应的属性及set方法
                ItemCat[] itemCats = objectMapper.readValue(JSONData, ItemCat[].class);
                return Arrays.asList(itemCats);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);

        //sql select * from tb_item_cat where parent_id = XXX
        return itemCatMapper.select(itemCat);
    }

    //查询全部商品的分类列表信息
    public ItemCatResult findItemCatAll() {
        /**
         * 查询要求：
         * 	1.只查询状态正常的数据
         */
        ItemCat itemCat = new ItemCat();
        itemCat.setStatus(1);        //表示状态正常的数据
        List<ItemCat> itemCatList = itemCatMapper.select(itemCat);

        //准备一级菜单数据
        Map<Long, List<ItemCat>> map = new HashMap<Long, List<ItemCat>>();

        for (ItemCat itemcat : itemCatList) {
            if (!map.containsKey(itemcat.getParentId())) {
                //表示没有该父级id
                map.put(itemcat.getParentId(), new ArrayList<ItemCat>());
            }
            //表示当前map集合中还有该上级id
            map.get(itemcat.getParentId()).add(itemcat);
        }
        //以上表示循环一次将全部的数据准备子父级关系

        //构建三级集合目录
        List<ItemCatData> itemCatDataList1 = new ArrayList();

        //循环遍历一级菜单
        for (ItemCat itemCat1 : map.get(0L)) {
            ItemCatData itemCatData1 = new ItemCatData(); //一级菜单
            itemCatData1.setUrl("/products/" + itemCat1.getId() + ".html");
            itemCatData1.setName("<a href='" + itemCatData1.getUrl() + "'>" + itemCat1.getName() + "</a>");

            //拼接二级菜单
            List<ItemCatData> itemCatDataList2 = new ArrayList<ItemCatData>();
            for (ItemCat itemCat2 : map.get(itemCat1.getId())) {
                //拼接2级菜单
                ItemCatData itemCatData2 = new ItemCatData();
                itemCatData2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatData2.setName(itemCat2.getName());

                //拼接三级菜单
                List<String> stringList3 = new ArrayList<String>();
                for (ItemCat itemCat3 : map.get(itemCat2.getId())) {
                    stringList3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                }

                itemCatData2.setItems(stringList3);
                itemCatDataList2.add(itemCatData2);
            }

            itemCatData1.setItems(itemCatDataList2);
            itemCatDataList1.add(itemCatData1);

            //如果数据大于14条则跳出循环
            if (itemCatDataList1.size() >= 14) {
                break;
            }
        }

        ItemCatResult itemCatResult = new ItemCatResult();
        itemCatResult.setItemCats(itemCatDataList1);

        return itemCatResult;
    }

}
