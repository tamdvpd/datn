package com.fashionstore.fashionstore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionstore.fashionstore.entity.Cart;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.repository.CartRepository;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.service.CartService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public List<Cart> getCartItems(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void addToCart(Integer userId, Integer productDetailId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new RuntimeException("Product detail not found"));

        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductDetailId(userId, productDetailId);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity); // cộng thêm số lượng
            cartRepository.save(cart);
        } else {
            Cart newCart = Cart.builder()
                    .user(user)
                    .productDetail(productDetail)
                    .quantity(quantity)
                    .build();
            cartRepository.save(newCart);
        }
    }

    @Override
    public void updateCartItem(Integer cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartRepository.delete(cart);
    }

    @Override
    public void clearCart(Integer userId) {
        cartRepository.deleteByUserId(userId);
    }
}

