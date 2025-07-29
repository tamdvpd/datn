package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Review;
import com.fashionstore.fashionstore.entity.ReviewImage;
import com.fashionstore.fashionstore.service.ReviewImageService;
import com.fashionstore.fashionstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/review-images")
@RequiredArgsConstructor
public class ReviewImageController {

    private final ReviewImageService reviewImageService;
    private final ReviewService reviewService;

    @Value("${upload.review.path:uploads/reviews}")
    private String uploadPath;

    // Lấy danh sách ảnh theo reviewId
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<ReviewImage>> getImagesByReview(@PathVariable Integer reviewId) {
        return ResponseEntity.ok(reviewImageService.getImagesByReviewId(reviewId));
    }

    // Upload ảnh vào 1 review cụ thể
    @PostMapping("/upload/{reviewId}")
    public ResponseEntity<?> uploadImage(@PathVariable Integer reviewId,
            @RequestParam("file") MultipartFile file) {
        try {
            Review review = reviewService.getReviewById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Review not found"));

            // Tạo thư mục nếu chưa có
            File dir = new File(uploadPath);
            if (!dir.exists())
                dir.mkdirs();

            // Tạo tên file duy nhất
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadPath + File.separator + fileName;

            // Lưu file vào thư mục
            file.transferTo(new File(filePath));

            // Lưu đường dẫn file vào DB
            ReviewImage image = ReviewImage.builder()
                    .review(review)
                    .imageUrl("/" + filePath.replace("\\", "/")) // đường dẫn cho FE
                    .build();

            return ResponseEntity.ok(reviewImageService.save(image));

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(404).body("Review not found");
        }
    }

    // Xoá ảnh
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer id) {
        reviewImageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
