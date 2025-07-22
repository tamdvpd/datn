package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Cart;
import com.fashionstore.fashionstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API
public class CartController {

    @Autowired
    private CartService cartService;

    // 🛒 Lấy danh sách giỏ hàng của user
    @GetMapping("/{userId}")
    public ResponseEntity<List<Cart>> getCart(@PathVariable Integer userId) {
        List<Cart> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    //  Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Integer userId,
                                            @RequestParam Integer productDetailId,
                                            @RequestParam Integer quantity) {
        cartService.addToCart(userId, productDetailId, quantity);
        return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng.");
    }

    //  Cập nhật số lượng sản phẩm trong giỏ
    @PutMapping("/update")
    public ResponseEntity<String> updateCart(@RequestParam Integer cartId,
                                             @RequestParam Integer quantity) {
        cartService.updateCartItem(cartId, quantity);
        return ResponseEntity.ok("Cập nhật số lượng thành công.");
    }

    //  Xóa 1 sản phẩm khỏi giỏ hàng
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCartItem(@RequestParam Integer cartId) {
        cartService.removeCartItem(cartId);
        return ResponseEntity.ok("Đã xóa sản phẩm khỏi giỏ hàng.");
    }

    //  Xóa toàn bộ giỏ hàng của user
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Đã xóa toàn bộ giỏ hàng.");
    }
}

