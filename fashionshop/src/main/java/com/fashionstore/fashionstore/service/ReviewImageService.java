package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.ReviewImage;

import java.util.List;

public interface ReviewImageService {
    List<ReviewImage> getImagesByReviewId(Integer reviewId);

    ReviewImage save(ReviewImage reviewImage);

    void deleteById(Integer id);
}
