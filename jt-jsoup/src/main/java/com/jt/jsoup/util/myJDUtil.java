package com.jt.jsoup.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.jsoup.pojo.Item;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class myJDUtil {
    private static final Logger logger = Logger.getLogger(myJDUtil.class);

    public static final ObjectMapper objectMapper = new ObjectMapper();


    //1.获取全部三级分类菜单
    public static List<String> getItemCat3List(String url) {
        List<String> itemCat3List = new ArrayList<String>();
        //获取页面对象和数据
        try {
            Elements elements = Jsoup.connect(url).get().select(".items dl dd a");
            for (Element element : elements) {
                String href = element.attr("href");
                if (href.startsWith("//list.jd.com/list.html?cat=")) {
                    itemCat3List.add("https:" + href);
                    System.out.println("获取的数据为https:" + href);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage() + url);

        }
        System.out.println("连接有" + itemCat3List.size() + "个");
        return itemCat3List;
    }

    public static List<String> itemCat3ListByPage(List<String> itemCat3List) {
        List<String> itemCat3ListPage = new ArrayList<String>();

        //获取每个三级商品分类的url
        for (String itemCat3Url : itemCat3List) {
            try {
                Element element = Jsoup.connect(itemCat3Url).get().select("#J_topPage span i").get(0);
                //表示获得每个连接下的总页面
                int pageCount = Integer.parseInt(element.text());
                for (int i = 1; i <= pageCount; i++) {
                    String pageUrl = itemCat3Url + "&page=" + i;
                    itemCat3ListPage.add(pageUrl);
                    //  System.out.println("获取每页的url"+pageUrl);
                }

            } catch (IOException e) {
                logger.info(e.getMessage());
                //  logger.info("有问题的连接为:" + itemCat3Url);
            }
        }
        System.out.println("总记录数:" + itemCat3ListPage.size());
        return itemCat3ListPage;
    }

    //3.获取每页时钟全部商品图片的url
    public static List<String> getItemUrl(List<String> itemCat3LisyByPage) {
        ArrayList<String> itemListUrl = new ArrayList<String>();
        for (String pageUrl : itemCat3LisyByPage) {
            try {
                Elements elements = Jsoup.connect(pageUrl).get().select("#plist ul li .p-img a");
                //循环遍历每一个商品信息
                for (Element element : elements) {
                    String itemHref = "https:" + element.attr("href");
                    System.out.println("商品的url:" + itemHref);
                    itemListUrl.add(itemHref);
                }
            } catch (IOException e) {
                logger.info(e.getMessage());
                logger.info("错误的url为:" + pageUrl);
            }
        }
        System.out.println("商品的总数为:" + itemListUrl.size());
        return itemListUrl;
    }

    public static Long getItemId(String url) {
        String itemId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        return Long.parseLong(itemId);
    }

    //获取商品的标题信息
    public static String getItemTitle(String url) {
        //获取页面的title
        Item item = new Item();

        try {
            Element element = Jsoup.connect(url).get().select(".sku-name").get(0);
            String title = element.text();
            return title;
            //2.获取商品卖点信息二次提交
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
        } catch (IOException e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    public static Long getItemPrice(String url) {
        Long itemId = getItemId(url);
        String priceUrl = "https://p.3.cn/prices/mgets?callback=jQuery6661675&type=1&area=1_72_2799_0&pdtk=&pduid=1476773839&pdpin=&pin=null&pdbp=0&skuIds=J_" + itemId + "&ext=11000000&source=item-pc";
        try {
            String JSON = Jsoup.connect(url).ignoreContentType(true).execute().body();
            String jsonTemp = JSON.substring(JSON.indexOf("{"), JSON.lastIndexOf("}") + 1);
            JsonNode jsonNode = objectMapper.readTree(jsonTemp);
            long price = jsonNode.get("op").asLong();
            return price;
        } catch (IOException e) {
            logger.info(e.getMessage());
            return 0L;
        }
    }

    public static String getItemImage(String url) {
        String imgUrl = null;
        try {
            Elements elements = Jsoup.connect(url).get().select(".lh li img");
            for (Element element : elements) {
                String imageTempUrl = "http" + element.attr("src");
                if (imgUrl != null) {
                    imgUrl += "," + imageTempUrl;
                } else {
                    imgUrl = imageTempUrl;
                }
            }
            return imgUrl;
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

    public static void main(String[] args) {
//        String url = "https://www.jd.com/allSort.aspx";
//        List<String> itemCat3List = getItemCat3List(url);
//        //获取的是每个商品每个页面的url
//        List<String> itemCat3Bypage = itemCat3ListByPage(itemCat3List);
//        //获取么一个商品的信息
//        List<String> itemUrl = getItemUrl(itemCat3Bypage);
        String url = "https://www.jd.com/allSort.aspx";
        List<String> itemCat3List = getItemCat3List(url);

        //获取的是每个商品每个页面的url
        List<String> itemCat3ByPage = itemCat3ListByPage(itemCat3List);

        //获取每一个商品的url
        List<String> itemUrl = getItemUrl(itemCat3ByPage);
    }

}
