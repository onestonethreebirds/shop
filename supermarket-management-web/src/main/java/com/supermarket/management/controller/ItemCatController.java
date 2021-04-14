package com.supermarket.management.controller;

import com.supermarket.management.pojo.ItemCat;
import com.supermarket.management.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
       // List<ItemCat> list = this.itemCatService.queryItemCatByPage(page, rows);
        List<ItemCat> list = this.itemCatService.queryByPage(page, rows);
        return list;
    }
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ItemCat> queryItemCatByParentId(@RequestParam(value = "id",defaultValue = "0") Long parentId){

       List<ItemCat> list = this.itemCatService.queryItemCatByParentId(parentId);

       return list;
    }
}
