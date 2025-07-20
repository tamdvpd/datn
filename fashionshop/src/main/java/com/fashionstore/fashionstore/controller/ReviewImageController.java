package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ReviewImage;
import com.fashionstore.fashionstore.service.ReviewImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3001") // Cho phép gọi từ frontend
@RestController
@RequestMapping("/api/review-images")
@RequiredArgsConstructor
public class ReviewImageController {

    private final ReviewImageService reviewImageService;

    // 🔍 Lấy danh sách ảnh theo reviewId
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<ReviewImage>> getImagesByReview(@PathVariable Integer reviewId) {
        List<ReviewImage> images = reviewImageService.getImagesByReviewId(reviewId);
        return ResponseEntity.ok(images);
    }

    // ➕ Thêm mới 1 ảnh (gửi JSON với reviewId và imageUrl)
    @PostMapping
    public ResponseEntity<ReviewImage> create(@RequestBody ReviewImage reviewImage) {
        ReviewImage savedImage = reviewImageService.save(reviewImage);
        return ResponseEntity.ok(savedImage);
    }

    // ❌ Xoá ảnh theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        reviewImageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
