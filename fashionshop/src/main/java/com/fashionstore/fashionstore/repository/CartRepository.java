package com.fashionstore.fashionstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fashionstore.fashionstore.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(Integer userId);
    Optional<Cart> findByUserIdAndProductDetailId(Integer userId, Integer productDetailId);
}
