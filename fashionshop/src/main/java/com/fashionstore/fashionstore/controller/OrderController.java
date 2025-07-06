package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.dto.CreateOrderRequest;
import com.fashionstore.fashionstore.dto.QuickBuyRequest;
import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.PaymentMethod;
import com.fashionstore.fashionstore.entity.ShippingProvider;
import com.fashionstore.fashionstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Integer id) {
        return ResponseEntity.of(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Integer id, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByUserAndStatus(@PathVariable Long userId,
            @PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByUserAndStatus(userId, status));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/quick-buy")
    public ResponseEntity<Integer> quickBuy(@RequestBody QuickBuyRequest req,
            @AuthenticationPrincipal 
            com.fashionstore.fashionstore.entity.User user) {
        Integer orderId = orderService.quickBuy(req, user);
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(orderId);
    }

}