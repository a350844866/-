package com.jt.web.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class TestHttpClient {
    /**
     * 1.定义httpClient
     * 2.定义访问的url
     * 3.定义请求方式
     * 4.发起http请求
     * 5.获取响应结果
     */
    @Test
    public void test01() throws IOException {
        //定义http请求对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //定义url
        String url = "http://tmooc.cn/web/index_new.html?tedu";

        //定义请求方式
        HttpGet httpGet = new HttpGet(url);

        //发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("恭喜您请求发送正确");
            String html = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(html);

        } else {
            System.out.println("请求失败");
        }
    }


    @Test
    public void testPost() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = "http://tmooc.cn/web/index_new.html?tedu";
        HttpPost post = new HttpPost(url);

        CloseableHttpResponse response = client.execute(post);

        String html = EntityUtils.toString(response.getEntity());
        System.out.println(html);


    }

}
