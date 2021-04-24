package com.supermarket.search.web.controller;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Item;
import com.supermarket.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_SUPERMARKET_ITEM_ROWS}")
    private Integer rows;

    @RequestMapping(method = RequestMethod.GET)
    public String search(Model model, @RequestParam("q") String query,
                         @RequestParam(value = "page",defaultValue = "1") Integer page) {

        try{
            query=new String(query.getBytes("ISO-8859-1"));
        }catch (Exception e){
            e.printStackTrace();
        }

        SupermarketResult<Item> supermarketResult=this.searchService.search(query,page,this.rows);
        model.addAttribute("query",query);
        model.addAttribute("itemList",supermarketResult.getRows());
        model.addAttribute("page",page);
        Long total = supermarketResult.getTotal();
        Long pages =(total+this.rows-1)/this.rows;
        model.addAttribute("totalPages",pages);

        return "search";
    }


}
