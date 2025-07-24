package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Cart;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.repository.CartRepository;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public List<Cart> getCartByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User không tồn tại");
        }
        return cartRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Cart addToCart(Integer userId, Integer productDetailId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User không tồn tại"));
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại"));
        if (productDetail.getQuantity() < quantity) {
            throw new IllegalArgumentException("Số lượng vượt quá tồn kho");
        }
        Optional<Cart> existing = cartRepository.findByUserIdAndProductDetailId(userId, productDetailId);
        if (existing.isPresent()) {
            Cart cart = existing.get();
            int newQuantity = cart.getQuantity() + quantity;
            if (productDetail.getQuantity() < newQuantity) {
                throw new IllegalArgumentException("Tổng số lượng vượt quá tồn kho");
            }
            cart.setQuantity(newQuantity);
            return cartRepository.save(cart);
        } else {
            Cart cart = Cart.builder()
                    .user(user)
                    .productDetail(productDetail)
                    .quantity(quantity)
                    .build();
            return cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public Cart updateCartItem(Integer cartId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm trong giỏ không tồn tại"));
        ProductDetail productDetail = cart.getProductDetail();
        if (productDetail.getQuantity() < quantity) {
            throw new IllegalArgumentException("Số lượng vượt quá tồn kho");
        }
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeCartItem(Integer cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new EntityNotFoundException("Sản phẩm trong giỏ không tồn tại");
        }
        cartRepository.deleteById(cartId);
    }

    @Override
    @Transactional
    public void clearCart(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User không tồn tại");
        }
        List<Cart> carts = cartRepository.findAll().stream()
                .filter(cart -> cart.getUser().getId().equals(userId))
                .toList();
        cartRepository.deleteAll(carts);
    }
}
