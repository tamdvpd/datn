package com.fashionstore.fashionstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fashionstore.fashionstore.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.productDetail pd JOIN FETCH pd.product WHERE c.user.id = :userId")
    List<Cart> findByUserId(@Param("userId") Integer userId);
    
    Optional<Cart> findByUserIdAndProductDetailId(Integer userId, Integer productDetailId);
}