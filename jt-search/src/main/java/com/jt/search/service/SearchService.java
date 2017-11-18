package com.jt.search.service;

import com.jt.search.pojo.Item;

import java.util.List;

public interface SearchService {
    @Deprecated
    List<Item> findItemListBykeyWord(String keyWord);

    List<Item> findItemListBykeyWord(String keyWord, Integer page, Integer rows);
}
