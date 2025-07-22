package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.entity.Wishlist;
import com.fashionstore.fashionstore.repository.ProductRepository;
import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.repository.WishlistRepository;
import com.fashionstore.fashionstore.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

            @Autowired
            private WishlistRepository wishlistRepository;
            @Autowired
        private UserRepository userRepository;

        @Autowired
        private ProductRepository productRepository;
    // ✅ CRUD methods
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
        Optional<Wishlist> existingWishlist = wishlistRepository.findById(id);
        if (existingWishlist.isPresent()) {
            Wishlist updatedWishlist = existingWishlist.get();
            updatedWishlist.setProduct(wishlist.getProduct());
            updatedWishlist.setUser(wishlist.getUser()); // nếu có quan hệ User
            return wishlistRepository.save(updatedWishlist);
        } else {
            throw new RuntimeException("Wishlist with ID " + id + " not found");
        }
    }

    @Override
    public void deleteWishlist(Integer id) {
        wishlistRepository.deleteById(id);
    }

    @Override
    public void addToWishlist(Long userId, Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToWishlist'");
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFromWishlist'");
    }

    @Override
    public List<Wishlist> getWishlistByUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWishlistByUser'");
    }


}
