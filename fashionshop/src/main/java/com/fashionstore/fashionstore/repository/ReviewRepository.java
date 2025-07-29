package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer>, JpaSpecificationExecutor<Review> {
    List<Review> findByProductDetailId(Integer productDetailId);

    List<Review> findByUserId(Long userId);
}
