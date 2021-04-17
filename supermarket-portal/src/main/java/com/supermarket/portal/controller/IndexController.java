package com.supermarket.portal.controller;

import com.supermarket.management.service.ContentService;
import org.apache.jute.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping("index")
public class IndexController {
    @Resource
    private ContentService contentService;
    @Value("${SUPERMARKET_AD_ID}")
    private Long SUPERMARKET_AD_ID;
    @RequestMapping(method = RequestMethod.GET)
    public String toIndex(Model model){
        // 调用内容服务，查询大广告数据,大广告分类id为31
        String AD = this.contentService.queryAD(this.SUPERMARKET_AD_ID);

        // 把大广告轮播数据放到Model中，传递给前台页面
        model.addAttribute("AD", AD);

        return "index";
    }

}
