package com.supermarket.management.service.impl;

import com.supermarket.management.service.TestService;
import com.supermarket.management.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestServiceImpl implements TestService {
    @Resource
private TestMapper testMapper;
    @Override
    public String queryDate() {
        return  testMapper.queryDate();
    }
}
