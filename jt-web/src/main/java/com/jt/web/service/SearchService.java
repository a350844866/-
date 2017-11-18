package com.jt.web.service;

import com.jt.web.pojo.Item;

import java.util.List;

public interface SearchService {
    @Deprecated
    List<Item> findItemListByKeyWord(String keyWord);

    List<Item> findItemListByKeyWord(String keyWord, Integer page, int rows);
}
