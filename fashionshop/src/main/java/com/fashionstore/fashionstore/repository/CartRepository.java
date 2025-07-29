package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    // Lấy danh sách giỏ hàng theo user ID
    List<Cart> findByUserId(Long userId);

    // Tìm sản phẩm cụ thể trong giỏ hàng của người dùng
    Cart findByUserIdAndProductDetailId(Long userId, Integer productDetailId);

    // Xoá toàn bộ giỏ hàng của người dùng
    void deleteByUserId(Long userId);
}
