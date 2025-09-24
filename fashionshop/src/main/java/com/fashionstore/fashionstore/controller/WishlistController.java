package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Wishlist;
import com.fashionstore.fashionstore.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists") // đổi thành /api/wishlists cho khớp frontend
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // Lấy tất cả wishlist (admin mới cần)
    @GetMapping
    public ResponseEntity<List<Wishlist>> getAll() {
        return ResponseEntity.ok(wishlistService.getAllWishlists());
    }

    // Lấy wishlist theo ID (ít dùng)
    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getById(@PathVariable Integer id) {
        return ResponseEntity.of(wishlistService.getWishlistById(id));
    }

    // 🔥 Lấy tất cả wishlist theo userId (quan trọng để hiển thị frontend)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUser(userId));
    }

    // Tạo mới (nếu bạn cần gọi trực tiếp)
    @PostMapping
    public ResponseEntity<Wishlist> create(@RequestBody Wishlist wishlist) {
        return ResponseEntity.ok(wishlistService.createWishlist(wishlist));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> update(@PathVariable Integer id, @RequestBody Wishlist wishlist) {
        return ResponseEntity.ok(wishlistService.updateWishlist(id, wishlist));
    }

    // Xóa wishlist theo id (Vue bạn đang gọi API này)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }

    // 🔥 Xóa wishlist theo userId + productId (nếu muốn)
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromWishlist(@RequestParam Integer userId,
                                                   @RequestParam Integer productId) {
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
