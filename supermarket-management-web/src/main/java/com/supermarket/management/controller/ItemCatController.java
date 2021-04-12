package com.supermarket.management.controller;

import com.supermarket.management.pojo.ItemCat;
import com.supermarket.management.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("item/cat")
@Controller
public class ItemCatController {

    @Resource
    private ItemCatService itemCatService;


    @RequestMapping("query/{page}")
    @ResponseBody
    public List<ItemCat> queryItemCatByPage(@PathVariable("page") Integer page, @RequestParam("rows") Integer rows){
        List<ItemCat> list = this.itemCatService.queryItemCatByPage(page, rows);
return list;
    }
}
