package com.supermarket.portal.controller;

import com.supermarket.cart.service.CartService;
import com.supermarket.management.pojo.Cart;
import com.supermarket.management.pojo.User;
import com.supermarket.portal.utils.CookieUtils;
import com.supermarket.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "order")
public class OrderController {
    @Value("${TT_TICKET}")
    private String TT_TICKET;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @RequestMapping(value = "create",method = RequestMethod.GET)
    public String create(HttpServletRequest request, Model model){
       // String ticket = CookieUtils.getCookieValue(request, this.TT_TICKET);
        //User user = this.userService.queryUserByTicket(ticket);
       // if(user!=null){
        User user = (User) request.getAttribute("user");
        List<Cart> carts = this.cartService.queryCartByUserId(user.getId());
            model.addAttribute("carts",carts);


        return "order-cart";

    }


}
