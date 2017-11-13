package com.jt.manage.controller.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.jt.web.service.ItemCatService;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.ItemCatResult;


@Controller
public class WebItemCatController {

    @Autowired(required = false)
    private ItemCatService itemCatService;

    private ObjectMapper objectMapper = new ObjectMapper();

    //查询商品分配目录
    //@RequestMapping("/web/itemcat/all")
    public void findItemCatAll1(String callback, HttpServletResponse response) {

        //拼接返回后的数据格式：category.getDataService(JSON);
        //获取三级分类目录
        ItemCatResult itemCatResult = itemCatService.findItemCatAll();
        try {
            String JSON = objectMapper.writeValueAsString(itemCatResult); //将数据转化为JSON串
            String callbackJSON = callback + "(" + JSON + ")";                 //拼接请求回调参数
            response.setContentType("text/html;charset=utf-8");              //设定字符集
            response.getWriter().write(callbackJSON);                     //回显参数
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //2.采用JSON对象实现JSONP的放回
    @RequestMapping("/web/itemcat/all")
    @ResponseBody
    public Object findItemCatAll(String callback, HttpServletResponse response) {
        //应该通过后台程序查询出全部的商品分类数据
        ItemCatResult itemCatList = itemCatService.findItemCatAll();

        MappingJacksonValue jacksonValue = new MappingJacksonValue(itemCatList);
        //实现JSONP的提交方式
        jacksonValue.setJsonpFunction(callback);
        return jacksonValue;
    }
}
