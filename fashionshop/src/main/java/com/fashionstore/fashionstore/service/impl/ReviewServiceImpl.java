package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Review;
import com.fashionstore.fashionstore.repository.ReviewRepository;
import com.fashionstore.fashionstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Page<Review> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Optional<Review> getReviewById(Integer id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Review createReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(null); // vì là đánh giá mới
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Integer id, Review updatedReview) {
        return reviewRepository.findById(id).map(existingReview -> {
            if (updatedReview.getRating() != null)
                existingReview.setRating(updatedReview.getRating());
            if (updatedReview.getComment() != null)
                existingReview.setComment(updatedReview.getComment());
            if (updatedReview.getImages() != null)
                existingReview.setImages(updatedReview.getImages());

            existingReview.setUpdatedAt(LocalDateTime.now());
            return reviewRepository.save(existingReview);
        }).orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    @Override
    public void deleteReview(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> getReviewsByProductDetailId(Integer productDetailId) {
        return reviewRepository.findByProductDetailId(productDetailId);
    }
}
