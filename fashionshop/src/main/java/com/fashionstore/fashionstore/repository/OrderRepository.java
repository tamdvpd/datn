package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 🔹 Lấy đơn hàng theo trạng thái (phân trang)
    Page<Order> findByStatus(String status, Pageable pageable);

    // 🔹 Lấy toàn bộ đơn hàng theo người dùng
    List<Order> findByUser(User user);

    // 🔹 Lấy đơn hàng theo người dùng và phân trang
    Page<Order> findByUser(User user, Pageable pageable);

    // 🔹 Lấy đơn hàng theo người dùng và trạng thái
    List<Order> findByUserAndStatus(User user, String status);

    // 🔹 Lấy đơn hàng theo user ID
    List<Order> findByUserId(Integer userId);
}
