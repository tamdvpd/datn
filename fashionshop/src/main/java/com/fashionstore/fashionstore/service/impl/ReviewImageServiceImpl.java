package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ReviewImage;
import com.fashionstore.fashionstore.repository.ReviewImageRepository;
import com.fashionstore.fashionstore.service.ReviewImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;

    @Override
    public List<ReviewImage> getImagesByReviewId(Integer reviewId) {
        return reviewImageRepository.findByReviewId(reviewId);
    }

    @Override
    public ReviewImage save(ReviewImage reviewImage) {
        return reviewImageRepository.save(reviewImage);
    }

    @Override
    public void deleteById(Integer id) {
        reviewImageRepository.deleteById(id);
    }
}
