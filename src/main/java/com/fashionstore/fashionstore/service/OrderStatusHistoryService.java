package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.OrderStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderStatusHistoryService {
    List<OrderStatusHistory> getAllOrderStatusHistorys();
    Page<OrderStatusHistory> getAllOrderStatusHistorys(Pageable pageable);
    Optional<OrderStatusHistory> getOrderStatusHistoryById(Integer id);
    OrderStatusHistory createOrderStatusHistory(OrderStatusHistory orderStatusHistory);
    OrderStatusHistory updateOrderStatusHistory(Integer id, OrderStatusHistory orderStatusHistory);
    void deleteOrderStatusHistory(Integer id);
}