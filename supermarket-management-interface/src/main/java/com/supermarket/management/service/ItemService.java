package com.supermarket.management.service;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Item;

public interface ItemService extends BaseService<Item> {
    public void saveItem(Item item,String Desc);

    SupermarketResult<Item> queryItemList(Integer page, Integer rows);
}
