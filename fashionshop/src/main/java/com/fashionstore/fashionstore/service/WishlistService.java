package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WishlistService {
    List<Wishlist> getAllWishlists();
    Page<Wishlist> getAllWishlists(Pageable pageable);
    Optional<Wishlist> getWishlistById(Integer id);
    Wishlist createWishlist(Wishlist wishlist);
    Wishlist updateWishlist(Integer id, Wishlist wishlist);
    void deleteWishlist(Integer id);
}