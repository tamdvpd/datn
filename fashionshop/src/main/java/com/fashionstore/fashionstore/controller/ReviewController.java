package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.Review;
import com.fashionstore.fashionstore.entity.ReviewImage;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.ReviewRepository;
import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001") // chỉnh lại frontend URL nếu cần
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductDetailRepository productDetailRepository;

    // ✅ Get all reviews (dùng cho admin)
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // ✅ Get paginated reviews
    @GetMapping("/page")
    public ResponseEntity<Page<Review>> getPagedReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewService.getAllReviews(pageable);
        return ResponseEntity.ok(reviewPage);
    }

    // ✅ Get review by ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Integer id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Create new review (client dùng)
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> payload) {
        try {
            Integer rating = (Integer) payload.get("rating");
            String comment = (String) payload.get("comment");
            Integer userId = (Integer) payload.get("userId");
            Integer productDetailId = (Integer) payload.get("productDetailId");

            if (userId == null || productDetailId == null) {
                return ResponseEntity.badRequest().body("User ID and ProductDetail ID are required.");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                    .orElseThrow(() -> new RuntimeException("ProductDetail not found"));

            Review review = new Review();
            review.setRating(rating);
            review.setComment(comment);
            review.setUser(user);
            review.setProductDetail(productDetail);
            review.setCreatedAt(LocalDateTime.now());

            List<Map<String, String>> images = (List<Map<String, String>>) payload.get("images");
            List<ReviewImage> reviewImages = new ArrayList<>();

            if (images != null) {
                for (Map<String, String> img : images) {
                    String imageUrl = img.get("imageUrl");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        ReviewImage image = new ReviewImage();
                        image.setImageUrl(imageUrl);
                        image.setCreatedAt(LocalDateTime.now());
                        image.setReview(review);
                        reviewImages.add(image);
                    }
                }
            }

            review.setReviewImages(reviewImages);
            Review savedReview = reviewRepository.save(review);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create review: " + e.getMessage());
        }
    }

    // ✅ Update review
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Integer id, @RequestBody Review review) {
        try {
            Review updated = reviewService.updateReview(id, review);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Delete review (admin dùng)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Get reviews theo product detail (client xem đánh giá sản phẩm)
    @GetMapping("/product/{productDetailId}")
    public ResponseEntity<List<Review>> getReviewsByProductDetail(@PathVariable Integer productDetailId) {
        List<Review> reviews = reviewService.getReviewsByProductDetailId(productDetailId);
        return ResponseEntity.ok(reviews);
    }
}
