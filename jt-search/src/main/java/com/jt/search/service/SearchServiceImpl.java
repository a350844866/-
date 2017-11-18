package com.jt.search.service;

import com.jt.common.service.HttpClientService;
import com.jt.search.pojo.Item;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private HttpSolrServer httpSolrServer;


    public List<Item> findItemListBykeyWord(String keyWord, Integer page, Integer rows) {
        //定义查询参数
        SolrQuery solrQuery = new SolrQuery();

        int startPage = (page - 1) * rows;


        //设置关键查询 根据关键字
        solrQuery.setQuery(keyWord);
        solrQuery.setStart(startPage);//定义分页开始
        solrQuery.setRows(rows);//定义分页的行数
        //获取solr的查询数据
        try {
            QueryResponse queryResponse = httpSolrServer.query(solrQuery);
            //获取itemList
            List<Item> itemList = queryResponse.getBeans(Item.class);
            return itemList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Item> findItemListBykeyWord(String keyWord) {
        //定义查询参数
        SolrQuery solrQuery = new SolrQuery();

        //设置关键查询 根据关键字
        solrQuery.setQuery(keyWord);
        solrQuery.setStart(0);//定义分页开始
        solrQuery.setRows(20);//定义分页的行数
        //获取solr的查询数据
        try {
            QueryResponse queryResponse = httpSolrServer.query(solrQuery);
            //获取itemList
            List<Item> itemList = queryResponse.getBeans(Item.class);
            return itemList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
