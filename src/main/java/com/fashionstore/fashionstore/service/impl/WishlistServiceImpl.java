package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Wishlist;
import com.fashionstore.fashionstore.repository.WishlistRepository;
import com.fashionstore.fashionstore.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public List<Wishlist> getAllWishlists() {
        return wishlistRepository.findAll();
    }

    @Override
    public Page<Wishlist> getAllWishlists(Pageable pageable) {
        return wishlistRepository.findAll(pageable);
    }

    @Override
    public Optional<Wishlist> getWishlistById(Integer id) {
        return wishlistRepository.findById(id);
    }

    @Override
    public Wishlist createWishlist(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist updateWishlist(Integer id, Wishlist wishlist) {
        wishlist.setId(id);
        return wishlistRepository.save(wishlist);
    }

    @Override
    public void deleteWishlist(Integer id) {
        wishlistRepository.deleteById(id);
    }
}