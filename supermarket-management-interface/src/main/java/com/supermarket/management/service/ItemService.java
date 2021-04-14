package com.supermarket.management.service;

import com.supermarket.management.pojo.Item;

public interface ItemService extends BaseService<Item> {
    public void saveItem(Item item,String Desc);
}
