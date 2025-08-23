package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {

    // ADMIN
    Page<Order> getAllOrders(Pageable pageable);

    Page<Order> getOrdersByStatus(String status, Pageable pageable);

    // USER
    Page<Order> getOrdersByUserId(Integer userId, Pageable pageable);

    Page<Order> getOrdersByUserIdAndStatus(Integer userId, String status, Pageable pageable);

    // CRUD
    Optional<Order> getOrderById(Integer id);

    Order createOrder(Order order);

    Order updateOrder(Integer id, Order order);

    Optional<Order> updateOrderStatus(Integer id, String newStatus);

    void deleteOrder(Integer id);

    boolean existsById(Integer id);

    // OrderService.java
    Optional<Order> getOrderByIdWithDetails(Integer id);

}