package com.supermarket.management.web.controller;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Item;
import com.supermarket.management.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("item")
public class ItemController {
    @Resource
    private ItemService itemService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public String saveItem(Item item,String desc){
        String msg="0";
        try {

            itemService.saveItem(item,desc);
        }catch (Exception e){
            msg="1";
            e.printStackTrace();
        }
        return msg;

    }
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public SupermarketResult<Item> queryItemList(@RequestParam(value = "page",defaultValue = "1") Integer page, @RequestParam(value = "rows",defaultValue = "30") Integer rows){
        SupermarketResult<Item> supermarketResult = this.itemService.queryItemList(page, rows);
        return supermarketResult;
    }

}
