package com.supermarket.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @RequestMapping(value = "page/{pageName}")
    public String toPage(@PathVariable("pageName") String pageName){
        return pageName;
    }

}
