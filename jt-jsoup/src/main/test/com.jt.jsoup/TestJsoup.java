package com.jt.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJsoup {


    //入门
    //@Test
    public void test01() throws IOException{
        String url ="https://item.jd.com/5148275.html";

        //通过jsoup模拟http请求消息
        Document document = Jsoup.connect(url).get();
        Elements elements = document.getAllElements();
        for (Element element : elements) {
            //表示获取页面中的全部div标签
            System.out.println(element.select("div"));
        }
    }


    //获取商品的标题信息
    //@Test
    public void testTitle() throws IOException{
        String url ="https://item.jd.com/5148275.html";
        Element element = Jsoup.connect(url).get().select(".sku-name").get(0);
        //Element element = Jsoup.connect(url).get().select(".price").get(0);

        String title = element.text();
        System.out.println(title);
    }

    @Test
    public void test02() throws IOException{
        String url ="https://item.jd.com/5148275.html";
        Element element = Jsoup.connect(url).get().select("#p-ad").get(0);
        String sellPoint = element.attr("title");
        System.out.println(sellPoint);

    }

    //获取商品的价格信息
    @Test
    public void testPrice() throws IOException{

        String url = "https://p.3.cn/prices/mgets?callback=jQuery4776426&type=1&area=1_72_4137_0&pdtk=&pduid=1550440883&pdpin=&pin=null&pdbp=0&skuIds=J_5148275&ext=11000000&source=item-pc";

        //jsoup的语法与JQuery类似，要按照Jquery的语法进行编译
        String JSON = Jsoup.connect(url).ignoreContentType(true).execute().body();
        String jsonTemp = JSON.substring(JSON.indexOf("{"), JSON.lastIndexOf("}")+1);
        ObjectMapper om = new ObjectMapper();
        String str = om.readTree(jsonTemp).get("op").asText();
        System.out.println(str);

    }

    @Test
    public void testImage() throws IOException{
        String url  = "https://item.jd.com/5148275.html";
        Elements elements = Jsoup.connect(url).get().select(".lh li img");
        //循环每一个elements标签
        for (Element element : elements) {
            String imgUrl = "http:" + element.attr("src");
            System.out.println("aa"+imgUrl);
        }
    }


}