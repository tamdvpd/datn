package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.repository.OrderDetailRepository;
import com.fashionstore.fashionstore.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @Override
    public Page<OrderDetail> getAllOrderDetails(Pageable pageable) {
        return orderDetailRepository.findAll(pageable);
    }

    @Override
    public Optional<OrderDetail> getOrderDetailById(Integer id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail updateOrderDetail(Integer id, OrderDetail orderDetail) {
        orderDetail.setId(id);
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(Integer id) {
        orderDetailRepository.deleteById(id);
    }
}