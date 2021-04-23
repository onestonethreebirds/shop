package com.supermarket.management.service.impl;

import com.supermarket.management.pojo.ItemCat;
import com.supermarket.management.service.ItemCatService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemCatServiceImpl extends BaseServiceImpl<ItemCat> implements ItemCatService{
    @Override
    public List<ItemCat> queryItemCatByParentId(Long parentId) {
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);
        List<ItemCat> list = super.queryListByWhere(itemCat);
        return list;
    }

    /**
  @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public List<ItemCat> queryItemCatByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        return this.itemCatMapper.select(null);
    }
    */
}
