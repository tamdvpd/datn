package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderStatus;

public interface OrderStatusHistoryService {
    void save(Order order, OrderStatus from, OrderStatus to, String note, Long changedBy);
}