package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Cart;
import com.fashionstore.fashionstore.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Integer userId) {
        try {
            List<Cart> carts = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(carts);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> body) {
        try {
            Integer userId = (Integer) body.get("userId");
            Integer productDetailId = (Integer) body.get("productDetailId");
            Integer quantity = (Integer) body.get("quantity");
            Cart cart = cartService.addToCart(userId, productDetailId, quantity);
            return ResponseEntity.status(HttpStatus.CREATED).body(cart);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error("Lỗi hệ thống"));
        }
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Integer cartId, @RequestBody Map<String, Object> body) {
        try {
            Integer quantity = (Integer) body.get("quantity");
            Cart cart = cartService.updateCartItem(cartId, quantity);
            return ResponseEntity.ok(cart);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error("Lỗi hệ thống"));
        }
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Integer cartId) {
        try {
            cartService.removeCartItem(cartId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(e.getMessage()));
        }
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Integer userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(e.getMessage()));
        }
    }

    private Map<String, String> error(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("error", message);
        return map;
    }
}
