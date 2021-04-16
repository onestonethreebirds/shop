package com.supermarket.management.service.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Content;
import com.supermarket.management.service.ContentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService{

    @Override
    public SupermarketResult<Content> queryContentList(Long categoryId,Integer page, Integer rows) {
        Content content = new Content();
        content.setCategoryId(categoryId);
        List<Content> list = super.queryListByWhere(content);
        PageHelper.startPage(page,rows);
        PageInfo<Content> pageInfo = new PageInfo<>(list);
        SupermarketResult<Content>supermarketResult=new SupermarketResult<>();
        supermarketResult.setTotal(pageInfo.getTotal());
        supermarketResult.setRows(list);
        return supermarketResult;
    }
}
