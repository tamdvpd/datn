package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.OrderStatusHistory;
import com.fashionstore.fashionstore.service.OrderStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderStatusHistorys")
@RequiredArgsConstructor
public class OrderStatusHistoryController {

    private final OrderStatusHistoryService orderStatusHistoryService;

    @GetMapping
    public ResponseEntity<List<OrderStatusHistory>> getAll() {
        return ResponseEntity.ok(orderStatusHistoryService.getAllOrderStatusHistorys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusHistory> getById(@PathVariable Integer id) {
        return ResponseEntity.of(orderStatusHistoryService.getOrderStatusHistoryById(id));
    }

    @PostMapping
    public ResponseEntity<OrderStatusHistory> create(@RequestBody OrderStatusHistory orderStatusHistory) {
        return ResponseEntity.ok(orderStatusHistoryService.createOrderStatusHistory(orderStatusHistory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderStatusHistory> update(@PathVariable Integer id, @RequestBody OrderStatusHistory orderStatusHistory) {
        return ResponseEntity.ok(orderStatusHistoryService.updateOrderStatusHistory(id, orderStatusHistory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        orderStatusHistoryService.deleteOrderStatusHistory(id);
        return ResponseEntity.noContent().build();
    }
}