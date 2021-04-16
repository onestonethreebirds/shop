package com.supermarket.management.service;

import com.supermarket.management.pojo.ContentCategory;

import java.util.List;

public interface ContentCategoryService extends BaseService<ContentCategory> {
    List<ContentCategory> queryContentCategoryListByParentId(Long parentId);

    ContentCategory saveContentCategory(ContentCategory contentCategory);

    void deleteContentCategory(Long parentId, Long id);

    /**
    void saveContentCategory(Long parentId, String name);

    void updateContentCategory(Long contentCategoryId, String name);

    void deleteContentCategory(Long id, Long parentId);
*/
     }
