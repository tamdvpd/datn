package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.repository.OrderRepository;
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

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Integer id, Order order) {
        order.setId(id);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}