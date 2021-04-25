package com.supermarket.portal.cookieService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.management.pojo.Cart;
import com.supermarket.management.pojo.Item;
import com.supermarket.management.service.ItemService;
import com.supermarket.portal.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartCookieService {


    private final static ObjectMapper MAPPER = new ObjectMapper();

    @Value("${TT_CART}")
    private String TT_CART;

    @Autowired
    private ItemService itemService;


    public void addItemByCookie(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 从cookie中获取购物车数据
        List<Cart> list =this.queryCartByCookie(request);
        Cart cart = null;
        // 遍历购物车数据
        for (Cart c : list) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
            }
        }

        if (cart != null) {
            // 如果存在则商品数量相加
            cart.setNum(cart.getNum() + num);
            cart.setUpdated(new Date());

        } else {
            // 如果不存在则把商品加入到购物车中
            // 获取商品数据
            Item item = this.itemService.queryById(itemId);

            // 设置购物车数据
            cart = new Cart();
            cart.setId(null);
            cart.setUserId(null);
            cart.setItemId(itemId);
            cart.setItemTitle(item.getTitle());
            // 设置商品图片url
            if (item.getImages() != null) {
                cart.setItemImage(item.getImages()[0]);
            } else {
                cart.setItemImage(null);
            }
            cart.setItemPrice(item.getPrice());
            cart.setNum(num);
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());

            // 把购物车放在list中
            list.add(cart);

        }

        // 把修改后的购物车数据放到cookie中
        try {
            CookieUtils.setCookie(request, response, this.TT_CART, MAPPER.writeValueAsString(list),
                    60 * 60 * 24 * 3, true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 查询cookie中的购物车数据
     *
     * @param request
     * @return
     */
    public List<Cart> queryCartByCookie(HttpServletRequest request) {
        // 从cookie获取购物车的json格式数据
        String json = CookieUtils.getCookieValue(request, this.TT_CART, true);

        // 判断json数据不为空
        List<Cart> list = null;
        if (StringUtils.isNotBlank(json)) {
            // 把json反序列化为list集合
            try {
                // json格式数据序列化为集合
                list = MAPPER.readValue(json,
                        MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));

                return list;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 如果json为空，返回空的list集合
        return new ArrayList<Cart>();
    }

    public void updateNumByCookie(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 从cookie获取购物车数据
        List<Cart> list = this.queryCartByCookie(request);

        // 遍历购物车
        for (Cart cart : list) {
            if (cart.getItemId().longValue() == itemId.longValue()) {
                // 如果找到对应的商品，修改商品数量，注意:不是相加
                cart.setNum(num);
                cart.setUpdated(new Date());

                // 把修改后的购物车数据保存在cookie中
                try {
                    CookieUtils.setCookie(request, response, this.TT_CART, MAPPER.writeValueAsString(list),
                            60 * 60 * 24 * 3, true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            }
        }

    }

    public void deleteItemByCookie(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        // 从cookie获取购物车数据
        List<Cart> list = this.queryCartByCookie(request);

        // 遍历购物车
        for (Cart cart : list) {
            if (cart.getItemId().longValue() == itemId.longValue()) {
                // 如果找到对应的商品，删除商品
                list.remove(cart);

                // 把删除后的购物车数据保存在cookie中
                try {
                    CookieUtils.setCookie(request, response, this.TT_CART, MAPPER.writeValueAsString(list),
                            60 * 60 * 24 * 3, true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            }
        }

    }

}


