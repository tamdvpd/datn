package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.dto.AddToCartRequest;

public interface CartService {
    void addToCart(Long userId, AddToCartRequest req);
}
