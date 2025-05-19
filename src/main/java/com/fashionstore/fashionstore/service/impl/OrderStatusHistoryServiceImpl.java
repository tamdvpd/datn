package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.OrderStatusHistory;
import com.fashionstore.fashionstore.repository.OrderStatusHistoryRepository;
import com.fashionstore.fashionstore.service.OrderStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Override
    public List<OrderStatusHistory> getAllOrderStatusHistorys() {
        return orderStatusHistoryRepository.findAll();
    }

    @Override
    public Page<OrderStatusHistory> getAllOrderStatusHistorys(Pageable pageable) {
        return orderStatusHistoryRepository.findAll(pageable);
    }

    @Override
    public Optional<OrderStatusHistory> getOrderStatusHistoryById(Integer id) {
        return orderStatusHistoryRepository.findById(id);
    }

    @Override
    public OrderStatusHistory createOrderStatusHistory(OrderStatusHistory orderStatusHistory) {
        return orderStatusHistoryRepository.save(orderStatusHistory);
    }

    @Override
    public OrderStatusHistory updateOrderStatusHistory(Integer id, OrderStatusHistory orderStatusHistory) {
        orderStatusHistory.setId(id);
        return orderStatusHistoryRepository.save(orderStatusHistory);
    }

    @Override
    public void deleteOrderStatusHistory(Integer id) {
        orderStatusHistoryRepository.deleteById(id);
    }
}