package com.fashionstore.fashionstore.service.impl;
import com.fashionstore.fashionstore.entity.*;
import com.fashionstore.fashionstore.repository.*;
import com.fashionstore.fashionstore.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepo.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public Order updateOrder(Integer id, Order order) {
        order.setId(id);
        return orderRepo.save(order);
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepo.deleteById(id);
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByUserAndStatus(Long userId, String status) {
        return orderRepo.findByUserIdAndStatus(userId, status);
    }
}
