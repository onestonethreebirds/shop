package com.supermarket.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    @RequestMapping(value = "page/{pageName}")
    public String toPage(@PathVariable("pageName") String pageName, Model model,
             @RequestParam(value = "redirectURL",defaultValue = "") String redirectURL){
        model.addAttribute("redirectURL",redirectURL);
        return pageName;
    }

}
