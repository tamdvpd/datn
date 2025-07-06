package com.fashionstore.fashionstore.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fashionstore.fashionstore.dto.AddToCartRequest;
import com.fashionstore.fashionstore.entity.Cart;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.repository.CartRepository;
import com.fashionstore.fashionstore.service.CartService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl {
    private final CartRepository cartRepo;
    private final UserRepository userRepo;
    private final ProductDetailRepository detailRepo;

    @Override
    public void addToCart(AddToCartRequest req, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        ProductDetail detail = detailRepo.findById(req.getProductDetailId())
                .orElseThrow(() -> new EntityNotFoundException("Variant not found"));

        Cart item = new Cart();
        item.setUser(user);
        item.setProductDetail(detail);
        item.setQuantity(req.getQuantity());
        item.setCreatedAt(LocalDateTime.now());
        cartRepo.save(item);
    }
}
