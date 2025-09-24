package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.entity.Wishlist;
import com.fashionstore.fashionstore.repository.ProductRepository;
import com.fashionstore.fashionstore.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Lấy tất cả wishlist
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

    // ================== CUSTOM METHOD ==================

    // Lấy tất cả wishlist theo userId
    @Override
    public List<Wishlist> getWishlistByUser(Integer userId) {
        return wishlistRepository.findAllByUserId(userId);
    }

    // Thêm sản phẩm vào wishlist (tránh trùng lặp)
    @Override
    public Wishlist addToWishlist(Integer userId, Integer productId) {
        // Nếu sản phẩm đã có trong wishlist thì trả về luôn
        Optional<Wishlist> existing = wishlistRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            return existing.get();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        return wishlistRepository.save(wishlist);
    }

    // Xóa sản phẩm khỏi wishlist
    @Override
    public void removeFromWishlist(Integer userId, Integer productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        wishlistRepository.deleteByUserAndProduct(user, product);
    }

    // Kiểm tra sản phẩm đã có trong wishlist chưa
    @Override
    public boolean existsInWishlist(Integer userId, Integer productId) {
        return wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
}
