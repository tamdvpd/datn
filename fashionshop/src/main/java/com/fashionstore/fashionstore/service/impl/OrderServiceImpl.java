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
        order.setId(null); // đảm bảo là create
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Integer id, Order order) {
        Optional<Order> existing = orderRepository.findById(id);
        if (existing.isPresent()) {
            order.setId(id);
            return orderRepository.save(order);
        }
        throw new IllegalArgumentException("Không tìm thấy đơn hàng có id = " + id);
    }

    @Override
    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Đơn hàng không tồn tại: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Order updateOrderStatus(Integer id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public Order updateTrackingCode(Integer id, String trackingCode) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setTrackingCode(trackingCode);
        return orderRepository.save(order);
    }

}
