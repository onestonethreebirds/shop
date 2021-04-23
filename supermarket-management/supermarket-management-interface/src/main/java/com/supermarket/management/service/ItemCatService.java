package com.supermarket.management.service;

import com.supermarket.management.pojo.ItemCat;

import java.util.List;

public interface ItemCatService extends BaseService<ItemCat>{
    List<ItemCat> queryItemCatByParentId(Long parentId);
}
