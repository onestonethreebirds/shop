package com.supermarket.management.service;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Content;

import java.util.List;

public interface ContentService extends BaseService<Content> {
    SupermarketResult<Content> queryContentList(Long categoryId,Integer page, Integer rows);

    String queryAD(Long categoryId);

    void deleteContent(Long id);
}
