package com.jt.jsoup.service;

import com.jt.jsoup.mapper.ItemMapper;
import com.jt.jsoup.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void saveItem() {

        Item item = new Item();
        item.setItemId(10000L);
        item.setTitle("0  ");
        item.setSellPoint("测试用例");
        item.setImage("测试");
        itemMapper.insert(item);
    }
}
