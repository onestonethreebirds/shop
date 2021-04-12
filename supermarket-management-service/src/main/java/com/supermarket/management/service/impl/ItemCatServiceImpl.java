package com.supermarket.management.service.impl;

import com.github.pagehelper.PageHelper;
import com.supermarket.management.mapper.ItemCatMapper;
import com.supermarket.management.pojo.ItemCat;
import com.supermarket.management.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService {
  @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public List<ItemCat> queryItemCatByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        return this.itemCatMapper.select(null);
    }
}
