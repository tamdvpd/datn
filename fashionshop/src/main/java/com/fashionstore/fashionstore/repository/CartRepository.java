package com.fashionstore.fashionstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fashionstore.fashionstore.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    
    // Tìm tất cả sản phẩm trong giỏ hàng của user
    List<Cart> findByUserId(Integer userId);

    // Tìm sản phẩm cụ thể trong giỏ của user (để kiểm tra đã tồn tại chưa)
    Optional<Cart> findByUserIdAndProductDetailId(Integer userId, Integer productDetailId);

    // Xóa tất cả sản phẩm trong giỏ hàng của user
    void deleteByUserId(Integer userId);
}
