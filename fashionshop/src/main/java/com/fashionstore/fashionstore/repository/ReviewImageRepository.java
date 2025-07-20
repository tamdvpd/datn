package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Integer> {

    // Lấy danh sách ảnh theo reviewId
    List<ReviewImage> findByReviewId(Integer reviewId);
}
