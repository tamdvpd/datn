package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Wishlist;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Optional<Wishlist> findByUserIdAndProductId(Integer userId, Integer productId);

    List<Wishlist> findAllByUserId(Integer userId);
}