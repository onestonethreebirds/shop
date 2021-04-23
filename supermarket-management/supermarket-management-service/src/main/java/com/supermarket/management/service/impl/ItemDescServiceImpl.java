package com.supermarket.management.service.impl;

import com.supermarket.management.pojo.ItemDesc;
import com.supermarket.management.service.ItemDescService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemDescServiceImpl extends BaseServiceImpl<ItemDesc> implements ItemDescService {

}
