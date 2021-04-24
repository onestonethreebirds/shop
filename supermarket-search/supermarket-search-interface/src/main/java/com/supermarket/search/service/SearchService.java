package com.supermarket.search.service;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Item;

public interface SearchService {
    SupermarketResult<Item> search(String query, Integer page, Integer rows);
}
