package com.jt.jsoup.service;

import com.jt.jsoup.mapper.ItemMapper;
import com.jt.jsoup.pojo.Item;
import com.jt.jsoup.util.JDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void saveItem() {
        String url = "https://www.jd.com/allSort.aspx";
        //获取全部的三级分类菜单
        List<String> itemCat3List = JDUtil.getItemCat3List(url);

        //获取的是每个商品每个页面的url
        List<String> itemCat3ByPage = JDUtil.itemCat3ListByPage(itemCat3List);

        //获取每一个商品的url
        List<String> itemUrl = JDUtil.getItemUrl(itemCat3ByPage);

        for (int i = 1; i < itemUrl.size(); i++) {
            Item item = JDUtil.getItem(itemUrl.get(i));
            itemMapper.insert(item);
            System.out.println("数据库插入" + i + "条数据");
        }
    }
}
