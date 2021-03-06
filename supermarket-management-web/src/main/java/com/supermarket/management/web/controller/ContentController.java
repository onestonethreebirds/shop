package com.supermarket.management.web.controller;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Content;
import com.supermarket.management.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@RequestMapping("content")
@Controller
public class ContentController {
    @Resource
    private ContentService contentService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void SaveContent(Content content) {
        // 调用服务保存
        this.contentService.save(content);

    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public SupermarketResult<Content> queryContentList(Long categoryId,
                                                       @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "20") Integer rows){


        SupermarketResult<Content> supermarketResult=this.contentService.queryContentList(categoryId,page,rows);

        return supermarketResult;

    }
    @RequestMapping(value = "edit",method = RequestMethod.POST)
    @ResponseBody
    public String edit(Content content){
        String status="200";
        try {
            this.contentService.updateByIdSelective(content);
        }catch (Exception e){
            e.printStackTrace();
            status="500";
        }

        return status;

    }
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public String delete(Long id){

        String status="200";
        try{
            this.contentService.deleteContent(id);
        }catch (Exception e){
            e.printStackTrace();
            status="500";
        }
        return status;

    }
}
