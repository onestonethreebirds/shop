package com.supermarket.management.web.controller;

import com.supermarket.management.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@RequestMapping("test")
@Controller
public class TestController {
    @Resource
    private TestService testService;
    @RequestMapping("date")
    @ResponseBody
    public String queryDate(){
        return this.testService.queryDate();
    }

}
