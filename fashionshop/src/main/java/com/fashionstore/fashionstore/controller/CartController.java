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

    @CrossOrigin(origins = "http://localhost:3001")
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
                System.out.println("Received request body: " + body);
                Object userIdObj = body.get("userId");
                Object productDetailIdObj = body.get("productDetailId");
                Object quantityObj = body.get("quantity");
                
                Integer userId = null;
                Integer productDetailId = null;
                Integer quantity = null;
                
                // Convert to Integer safely
                if (userIdObj instanceof Number) {
                    userId = ((Number) userIdObj).intValue();
                } else if (userIdObj instanceof String) {
                    userId = Integer.parseInt((String) userIdObj);
                }
                
                if (productDetailIdObj instanceof Number) {
                    productDetailId = ((Number) productDetailIdObj).intValue();
                } else if (productDetailIdObj instanceof String) {
                    productDetailId = Integer.parseInt((String) productDetailIdObj);
                }
                
                if (quantityObj instanceof Number) {
                    quantity = ((Number) quantityObj).intValue();
                } else if (quantityObj instanceof String) {
                    quantity = Integer.parseInt((String) quantityObj);
                }
                
                System.out.println("Parsed values - userId: " + userId + ", productDetailId: " + productDetailId + ", quantity: " + quantity);
                
                if (userId == null || productDetailId == null || quantity == null) {
                    return ResponseEntity.badRequest().body(error("Thiếu thông tin bắt buộc"));
                }
                
                Cart cart = cartService.addToCart(userId, productDetailId, quantity);
                return ResponseEntity.status(HttpStatus.CREATED).body(cart);
            } catch (EntityNotFoundException | IllegalArgumentException e) {
                System.out.println("Error in addToCart: " + e.getMessage());
                return ResponseEntity.badRequest().body(error(e.getMessage()));
            } catch (Exception e) {
                System.out.println("Unexpected error in addToCart: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error("Lỗi hệ thống: " + e.getMessage()));
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