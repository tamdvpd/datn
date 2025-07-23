package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {
    List<OrderDetail> getAllOrderDetails();
    Page<OrderDetail> getAllOrderDetails(Pageable pageable);
    Optional<OrderDetail> getOrderDetailById(Integer id);
    OrderDetail createOrderDetail(OrderDetail orderDetail);
    OrderDetail updateOrderDetail(Integer id, OrderDetail orderDetail);
    void deleteOrderDetail(Integer id);
    
}