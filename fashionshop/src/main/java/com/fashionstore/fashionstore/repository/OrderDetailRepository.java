package com.fashionstore.fashionstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fashionstore.fashionstore.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    List<OrderDetail> findByOrder_Id(Integer orderId);

    // ✅ Kiểm tra sản phẩm đã được dùng trong OrderDetail hay chưa
    boolean existsByProductDetail_Product_Id(Integer productId);
}