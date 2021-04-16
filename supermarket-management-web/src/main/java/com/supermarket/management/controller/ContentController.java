package com.supermarket.management.controller;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Content;
import com.supermarket.management.pojo.ContentCategory;
import com.supermarket.management.service.ContentCategoryService;
import com.supermarket.management.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("content")
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void SaveContent(Content content) {
        // 调用服务保存
        this.contentService.save(content);

    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public SupermarketResult<Content> queryContentList(@RequestParam(value = "id",defaultValue = "0") Long categoryId,
                                                       @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "20") Integer rows){


        SupermarketResult<Content> supermarketResult=this.contentService.queryContentList(categoryId,page,rows);

        return supermarketResult;

    }
}
