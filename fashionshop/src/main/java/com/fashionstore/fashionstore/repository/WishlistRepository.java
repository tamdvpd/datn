package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Wishlist;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Optional<Wishlist> findByUserIdAndProductId(Integer userId, Integer productId);

    // JOIN FETCH để lấy luôn product (tránh lazy null)
    @Query("SELECT w FROM Wishlist w JOIN FETCH w.product WHERE w.user.id = :userId")
    List<Wishlist> findAllByUserId(@Param("userId") Integer userId);

    void deleteByUserAndProduct(User user, Product product);
}
