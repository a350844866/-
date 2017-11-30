package com.jt.jsoup.controller;

import com.jt.jsoup.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping
    @ResponseBody
    public String saveItem() {
        itemService.saveItem();
        System.out.println("成功");
        return "成功";
    }


}
