package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Page<Order> getAllOrders(Pageable pageable);
    Optional<Order> getOrderById(Integer id);
    Order createOrder(Order order);
    Order updateOrder(Integer id, Order order);
    void deleteOrder(Integer id);
}