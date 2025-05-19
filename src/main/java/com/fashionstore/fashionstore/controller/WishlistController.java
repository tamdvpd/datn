package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Wishlist;
import com.fashionstore.fashionstore.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<Wishlist>> getAll() {
        return ResponseEntity.ok(wishlistService.getAllWishlists());
    }

    @GetMapping("/{id}")

    public ResponseEntity<Wishlist> getById(@PathVariable Integer id) {
        return ResponseEntity.of(wishlistService.getWishlistById(id));
    }

    @PostMapping
    public ResponseEntity<Wishlist> create(@RequestBody Wishlist wishlist) {
        return ResponseEntity.ok(wishlistService.createWishlist(wishlist));
    }

    @PutMapping("/{id}")

    public ResponseEntity<Wishlist> update(@PathVariable Integer id, @RequestBody Wishlist wishlist) {
        return ResponseEntity.ok(wishlistService.updateWishlist(id, wishlist));
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }
}