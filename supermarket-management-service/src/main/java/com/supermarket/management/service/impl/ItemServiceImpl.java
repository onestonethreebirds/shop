package com.supermarket.management.service.impl;

import com.supermarket.management.pojo.Item;
import com.supermarket.management.pojo.ItemDesc;
import com.supermarket.management.service.ItemCatService;
import com.supermarket.management.service.ItemDescService;
import com.supermarket.management.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {

    @Resource
    private ItemDescService itemDescService;
    @Override
    public void saveItem(Item item,String desc) {
        item.setStatus(1);
        super.save(item);
        ItemDesc itemDesc = new ItemDesc();

        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.save(itemDesc);

    }
}
