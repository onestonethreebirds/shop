package com.supermarket.cart.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.cart.redis.RedisUtils;
import com.supermarket.cart.service.CartService;
import com.supermarket.management.mapper.ItemMapper;
import com.supermarket.management.pojo.Cart;
import com.supermarket.management.pojo.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private static final ObjectMapper MAPPER= new ObjectMapper();

    @Value("${SUPERMARKET_CART_KEY}")
    private String SUPERMARKET_CART_KEY;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void saveItemByCart(Long userId, Long itemId, Integer num) {
// 从redis中查询购物车数据。redis中保存的是购物车的json格式数据
        List<Cart> list = this.queryCartByUserId(userId);
        // 遍历购物车，商品是否存在

        Cart cart = null;
        for (Cart c : list) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
            }
        }
        if (cart != null) {
            cart.setNum(cart.getNum() + num);
            cart.setUpdated(new Date());
        } else {
            Item item = this.itemMapper.selectByPrimaryKey(itemId);
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItemId(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImage());
            cart.setNum(num);
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());

            list.add(cart);

        }
        try {
            // 把添加好的购物车保存在redis中
            this.redisUtils.set(this.SUPERMARKET_CART_KEY+userId, MAPPER.writeValueAsString(list));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cart> queryCartByUserId(Long userId) {
        // 从redis中查询购物车数据
        String json = this.redisUtils.get(this.SUPERMARKET_CART_KEY + userId);
        // 判断json数据不为空
        if(StringUtils.isNotBlank(json)){
            // 把json反序列化为list集合
            try{
                // json格式数据序列化为集合
                List<Cart> list = MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
                return list;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new ArrayList<Cart>();
    }

    @Override
    public void updateNumByCart(Long userId, Long itemId, Integer num) {
        // 从redis中查询购物车数据。redis中保存的是购物车的json格式数据
        List<Cart> list = this.queryCartByUserId(userId);
// 遍历购物车，商品是否存在,存在则更新，不存在什么都不做
        for (Cart cart : list) {
            if (cart.getItemId().longValue() == itemId.longValue()) {
                // 如果存在，则更新购物车商品数量。这里是更新，不是相加
                cart.setNum(num);
                cart.setUpdated(new Date());

                try {
                    // 把添加后的购物车保存在redis中
                    this.redisUtils.set(this.SUPERMARKET_CART_KEY + userId, MAPPER.writeValueAsString(list));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteItemByCart(Long userId, Long itemId) {
        // 从redis中查询购物车数据。redis中保存的是购物车的json格式数据
        List<Cart> list = this.queryCartByUserId(userId);

        // 遍历购物车，商品是否存在,存在则更新，不存在什么都不做
        for (Cart cart : list) {
            if (cart.getItemId().longValue() == itemId.longValue()) {
                list.remove(cart);

                try {
                    // 把删除后的购物车保存在redis中
                    this.redisUtils.set(this.SUPERMARKET_CART_KEY + userId, MAPPER.writeValueAsString(list));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // 跳出循环
                break;
            }
        }


    }


}

