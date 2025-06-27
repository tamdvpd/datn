package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Long userId);
    List<Order> findByUserIdAndStatus(Long userId, String status);

}
