package com.jt.jsoup.util;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.jsoup.pojo.Item;

public class JDUtil {

    private static final Logger logger = Logger.getLogger(JDUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //1.获取全部三级分类菜单
    public static List<String> getItemCat3List(String url) {
        List<String> itemCat3List = new ArrayList<String>();

        try {
            //获取页面对象和数据
            Elements elements = Jsoup.connect(url).get().select(".items dl dd a");

            for (Element element : elements) {

                String href = element.attr("href");
                if (href.startsWith("//list.jd.com/list.html?cat=")) {
                    //该链接才满足个数
                    String item3Url = "https:" + href;
                    itemCat3List.add(item3Url);  //https://list.jd.com/list.html?cat=

                    //System.out.println("获取的数据为"+item3Url);
                }
            }
        } catch (Exception e) {
            //目的放爬虫程序正常的执行
            e.printStackTrace();
            logger.info(e.getMessage() + url);
        }

        //System.out.println("获取有效连接数为:"+itemCat3List.size());
        return itemCat3List;
    }

    //2.获取每个分类商品每一页商品的url
    //先获取每页的总数
    //https://list.jd.com/list.html?cat=1319,6313,11235
    //https://list.jd.com/list.html?cat=1713,4855,4859&page=3

    public static List<String> itemCat3ListByPage(List<String> itemCat3List) {
        List<String> itemCat3ListByPage = new ArrayList<String>();

        //获取每一个三级商品分类的url
        for (String itemCat3Url : itemCat3List) {

            try {
                Element element = Jsoup.connect(itemCat3Url).get().select("#J_topPage span i").get(0);

                //表示获取每个连接下的总页面
                int pageCount = Integer.parseInt(element.text());
                for (int i = 1; i <= pageCount; i++) {
                    String pageUrl = itemCat3Url + "&page=" + i;

                    //System.out.println("获取每页url:"+pageUrl);
                    itemCat3ListByPage.add(pageUrl);
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
                logger.info("有问题的链接为:" + itemCat3Url);
            }
        }

        //System.out.println("总记录数:"+itemCat3ListByPage.size());
        return itemCat3ListByPage;
    }


    //3.获取每页中全部商品的url
    public static List<String> getItemUrl(List<String> itemCat3ListByPage) {
        ArrayList<String> itemListUrl = new ArrayList<String>();

        for (int i = 0; i < 5; i++) {

            try {
                Elements elements = Jsoup.connect(itemCat3ListByPage.get(i)).get().select("#plist ul li .p-img a");

                //循环遍历每一个商品信息
                for (Element element : elements) {
                    String itemHref = "https:" + element.attr("href");

                    System.out.println("商品的url:" + itemHref);
                    itemListUrl.add(itemHref);
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
                logger.info("错误的url为:" + itemCat3ListByPage.get(i));
            }
        }

        System.out.println("商品的总数为:" + itemListUrl.size());
        return itemListUrl;

    }

    //将item值转化为Long类型数据
    //url:https://item.jd.com/12324671522.html
    public static Long getItemId(String url) {

        String itemId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));

        return Long.parseLong(itemId);
    }


    //获取商品的标题信息
    public static String getItemTitle(String url) {

        try {
            Element element = Jsoup.connect(url).get().select(".sku-name").get(0);
            String title = element.text();
            return title;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    public static String getItemSellPoint(String url) {
        //获取商品的id
        Long itemId = getItemId(url);
        String urlSell = "http://ad.3.cn/ads/mgets?skuids=AD_" + itemId + ",AD_" + itemId;

        try {
            String resultJSON = Jsoup.connect(urlSell).ignoreContentType(true).execute().body();
            JsonNode jsonNode = objectMapper.readTree(resultJSON).get(0);
            String sellPoint = jsonNode.get("ad").asText();

            return sellPoint;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }

    }


    //获取商品的价格信息
    public static Long getItemPrice(String url) {
        Long itemId = getItemId(url);
        String priceUrl = "https://p.3.cn/prices/mgets?callback=jQuery6661675&type=1&area=1_72_2799_0&pdtk=&pduid=1476773839&pdpin=&pin=null&pdbp=0&skuIds=J_" + itemId + "&ext=11000000&source=item-pc";

        try {
            String JSON = Jsoup.connect(url).ignoreContentType(true).execute().body();
            String jsonTemp = JSON.substring(JSON.indexOf("{"), JSON.lastIndexOf("}") + 1);
            JsonNode jsonNode = objectMapper.readTree(jsonTemp);
            Long price = jsonNode.get("op").asLong();
            return price;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return 0L;
        }
    }


    public static String getItemImage(String url) {

        String imageUrl = null;
        try {
            //获取商品的image
            Elements elements = Jsoup.connect(url).get().select(".lh li img");
            for (Element element : elements) {
                String imageTempUrl = "http:" + element.attr("src");

                if (imageUrl != null) {
                    imageUrl += "," + imageTempUrl;
                } else {
                    imageUrl = imageTempUrl;
                }
            }
            return imageUrl;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    //获取每个页面中的信息封装为Item对象
    public static Item getItem(String url) {

        Item item = new Item();

        //1.添加商品的title
        item.setTitle(getItemTitle(url));

        //2.获取商品卖点信息二次提交
        item.setSellPoint(getItemSellPoint(url));

        //3.获取商品的价格
        item.setPrice(getItemPrice(url));

        //4.添加商品的图片信息
        item.setImage(getItemImage(url));

        //5.添加商品的itemId
        item.setItemId(getItemId(url));

        item.setCreated(new Date());
        item.setUpdated(item.getCreated());

        return item;
    }

    //测试代码
    public static void main(String[] args) {
        String url = "https://www.jd.com/allSort.aspx";
        List<String> itemCat3List = getItemCat3List(url);

        //获取的是每个商品每个页面的url
        List<String> itemCat3ByPage = itemCat3ListByPage(itemCat3List);

        //获取每一个商品的url
        List<String> itemUrl = getItemUrl(itemCat3ByPage);
    }


}

