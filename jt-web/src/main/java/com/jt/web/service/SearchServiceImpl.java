package com.jt.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private HttpClientService httpClientService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final Logger logger = Logger.getLogger(SearchServiceImpl.class);

    @Override
    public List<Item> findItemListByKeyWord(String keyWord, Integer page, int rows) {
        List<Item> itemList = new ArrayList<>();

        //1.定义url
        String url = "http://search.jt.com/search";

        //2.封装参数
        Map<String, String> searMap = new HashMap<String, String>();

        searMap.put("keyWord", keyWord);
        searMap.put("page", page + "");
        searMap.put("rows", rows + "");

        try {
            //返回结果是List<item>的JSON
            String itemJSON = httpClientService.doPost(url, searMap, "utf-8");

            //需要检查返回的结果是否包含全部数据 如果没有全部数据 需要将JSON的返回值定义为忽略状态
            Item[] items = objectMapper.readValue(itemJSON, Item[].class);

            for (Item item : items) {
                itemList.add(item);
            }
            return itemList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //完成根据关键字检索的操作
    @Override
    public List<Item> findItemListByKeyWord(String keyWord) {
        List<Item> itemList = new ArrayList<>();

        //1.定义url
        String url = "http://search.jt.com/search";

        //2.封装参数
        Map<String, String> searMap = new HashMap<String, String>();

        searMap.put("keyWord", keyWord);

        try {
            //返回结果是List<item>的JSON
            String itemJSON = httpClientService.doPost(url, searMap, "utf-8");

            //需要检查返回的结果是否包含全部数据 如果没有全部数据 需要将JSON的返回值定义为忽略状态
            Item[] items = objectMapper.readValue(itemJSON, Item[].class);

            for (Item item : items) {
                itemList.add(item);
            }
            return itemList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
