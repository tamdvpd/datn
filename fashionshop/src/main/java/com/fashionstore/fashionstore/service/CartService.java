package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Cart;
import java.util.List;

public interface CartService {
    List<Cart> getCartByUserId(Integer userId);
    Cart addToCart(Integer userId, Integer productDetailId, Integer quantity);
    Cart updateCartItem(Integer cartId, Integer quantity);
    void removeCartItem(Integer cartId);
    void clearCart(Integer userId);
}
