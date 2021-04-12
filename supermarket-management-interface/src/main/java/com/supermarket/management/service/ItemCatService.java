package com.supermarket.management.service;

import com.supermarket.management.pojo.ItemCat;

import java.util.List;

public interface ItemCatService {
    public List<ItemCat> queryItemCatByPage(Integer page,Integer rows);
}
