package com.jt.web.controller;

import com.jt.web.pojo.Item;
import com.jt.web.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String toSearch(@RequestParam("q") String keyWord, Model model, @RequestParam(defaultValue = "1") Integer page) {
        try {
            keyWord = new String(keyWord.getBytes("ISO-8859-1"), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int rows = 20;
        List<Item> itemList = searchService.findItemListByKeyWord(keyWord, page, rows);
        model.addAttribute("itemList", itemList);
        //准备页面数据
        model.addAttribute("query", keyWord);

        //表示跳转到全文检索的展现页面
        return "search";
    }
}
