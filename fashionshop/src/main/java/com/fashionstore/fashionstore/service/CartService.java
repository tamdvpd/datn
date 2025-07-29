package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Cart;
import java.util.List;

public interface CartService {
    List<Cart> getCartsByUserId(Long userId);

    Cart addToCart(Long userId, Integer productDetailId, Integer quantity);

    void removeFromCart(Integer cartId);

    void clearCart(Long userId);
}
