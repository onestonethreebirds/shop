package com.supermarket.cart.service;

import com.supermarket.management.pojo.Cart;

import java.util.List;

public interface CartService {
    void saveItemByCart(Long userId, Long itemId, Integer num);

    List<Cart> queryCartByUserId(Long userId);

    void updateNumByCart(Long userId, Long itemId, Integer num);

    void deleteItemByCart(Long userId, Long itemId);
}
