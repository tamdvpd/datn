package com.fashionstore.fashionstore.service;

import java.util.List;

import com.fashionstore.fashionstore.entity.Cart;

public interface CartService {
    // Lấy tất cả sản phẩm trong giỏ hàng của user
    List<Cart> getCartItems(Integer userId);

    // Thêm sản phẩm vào giỏ hàng
    void addToCart(Integer userId, Integer productDetailId, Integer quantity);

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    void updateCartItem(Integer cartId, Integer quantity);

    // Xóa 1 sản phẩm khỏi giỏ hàng
    void removeCartItem(Integer cartId);

    // Xóa toàn bộ giỏ hàng của user
    void clearCart(Integer userId);
}

