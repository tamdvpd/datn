package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderDetails")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @GetMapping
    public ResponseEntity<List<OrderDetail>> getAll() {
        return ResponseEntity.ok(orderDetailService.getAllOrderDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getById(@PathVariable Integer id) {
        return ResponseEntity.of(orderDetailService.getOrderDetailById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDetail> create(@RequestBody OrderDetail orderDetail) {
        return ResponseEntity.ok(orderDetailService.createOrderDetail(orderDetail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetail> update(@PathVariable Integer id, @RequestBody OrderDetail orderDetail) {
        return ResponseEntity.ok(orderDetailService.updateOrderDetail(id, orderDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }
}