package com.supermarket.portal.controller;

import com.supermarket.cart.service.CartService;
import com.supermarket.management.pojo.Cart;
import com.supermarket.management.pojo.User;
import com.supermarket.portal.cookieService.CartCookieService;
import com.supermarket.portal.utils.CookieUtils;
import com.supermarket.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("cart")
public class CartController {
    @Value("${TT_TICKET}")
    private String TT_TICKET;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;
    @Autowired
    private CartCookieService cartCookieService;

    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public String saveItemByCart(@PathVariable(value = "itemId") Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){

        //获取用户信息
        String ticket = CookieUtils.getCookieValue(request, this.TT_TICKET);
        User user = this.userService.queryUserByTicket(ticket);

        if(user!=null){
            this.cartService.saveItemByCart(user.getId(),itemId,num);
        }else {

            this.cartCookieService.addItemByCookie(itemId,num,request,response);

        }
return "redirect:/cart/show.html";
    }

    @RequestMapping(value = "show",method = RequestMethod.GET)
    public String showCart(Model model,HttpServletRequest request){
        String ticket = CookieUtils.getCookieValue(request, this.TT_TICKET);
        User user = userService.queryUserByTicket(ticket);
        List<Cart> cartList=null;
        if(user!=null){
            cartList=this.cartService.queryCartByUserId(user.getId());
        }else {
            this.cartCookieService.queryCartByCookie(request);

        }
        model.addAttribute("cartList",cartList);
        return "cart";
    }
    // 请求方法：post
    // 请求url:/service/cart/update/num/{itemId}/{num}
    /**
     * 更新购物车商品数量
     *
     * @param itemId
     * @param num
     * @param request
     */
    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    @ResponseBody
    public String updateNumByCart(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,
                                HttpServletRequest request,HttpServletResponse response) {
        // 获取用户登录信息
        String ticket = CookieUtils.getCookieValue(request, this.TT_TICKET);
        User user = this.userService.queryUserByTicket(ticket);

        // 判断用户是否登录
        if (user != null) {
            // 用户已登录
            this.cartService.updateNumByCart(user.getId(), itemId, num);

        } else {
            // 用户未登录
            this.cartCookieService.updateNumByCookie(itemId, num, request, response);

        }
        return "ok";

    }

    // http://www.supermarket.com/cart/delete/1082433.html
    /**
     * 删除购物车的商品
     *
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteItemByCart(@PathVariable("itemId") Long itemId, HttpServletRequest request,HttpServletResponse response) {
        // 获取用户登录信息
        String ticket = CookieUtils.getCookieValue(request, this.TT_TICKET);
        User user = this.userService.queryUserByTicket(ticket);

        // 判断用户是否登录
        if (user != null) {
            // 用户已登录
            this.cartService.deleteItemByCart(user.getId(), itemId);

        } else {
            // 用户未登录
            this.cartCookieService.deleteItemByCookie(itemId, request, response);


        }

        return "redirect:/cart/show.html";
    }


}
