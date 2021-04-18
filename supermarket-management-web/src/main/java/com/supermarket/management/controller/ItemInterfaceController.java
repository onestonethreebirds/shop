package com.supermarket.management.controller;

import com.supermarket.management.pojo.Item;
import com.supermarket.management.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("item/interface")
@Controller
public class ItemInterfaceController {
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<Item> queryItemById(@PathVariable("id") Long id){

        try{
            Item item = this.itemService.queryById(id);
            return ResponseEntity.ok().body(item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item){
        try {
            this.itemService.save(item);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
@RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item){
        try {
            this.itemService.updateByIdSelective(item);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteItemById(@PathVariable("id") Long id) {
        try {
            this.itemService.deleteById(id);
            // 返回204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 如果有异常，设置状态码为500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }



}
