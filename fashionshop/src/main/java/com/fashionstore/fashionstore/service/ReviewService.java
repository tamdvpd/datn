package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();

    Page<Review> getAllReviews(Pageable pageable);

    Optional<Review> getReviewById(Integer id);

    Review createReview(Review review);

    Review updateReview(Integer id, Review updatedReview);

    void deleteReview(Integer id);

    List<Review> getReviewsByProductDetailId(Integer productDetailId);
}
