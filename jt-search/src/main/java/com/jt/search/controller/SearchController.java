package com.jt.search.controller;

import com.jt.search.pojo.Item;
import com.jt.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    @ResponseBody
    public List<Item> findItemListBykeyWord(String keyWord, Integer page, Integer rows) {
        List<Item> itemList = searchService.findItemListBykeyWord(keyWord, page, rows);
        return itemList;
    }
}
