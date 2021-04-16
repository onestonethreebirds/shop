package com.supermarket.management.service;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Content;

public interface ContentService extends BaseService<Content> {
    SupermarketResult<Content> queryContentList(Long categoryId,Integer page, Integer rows);
}
