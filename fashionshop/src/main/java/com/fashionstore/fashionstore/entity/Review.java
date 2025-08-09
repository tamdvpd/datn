package com.fashionstore.fashionstore.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Reviews")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Người dùng đánh giá
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({
            "password", "orders", "wishlist", "importInvoices",
            "reviews", "hibernateLazyInitializer", "handler"
    })
    private User user;

    // Biến thể sản phẩm được đánh giá
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    @JsonIgnoreProperties({
            "reviews", "product", "importInvoiceDetails", "orderDetails",
            "hibernateLazyInitializer", "handler"
    })
    private ProductDetail productDetail;

    @Column(nullable = false)
    private Integer rating; // số sao

    @Column(length = 1000)
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Danh sách ảnh đánh giá
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ReviewImage> reviewImages = new ArrayList<>();

    // Trả về danh sách URL ảnh (không lưu trong DB)
    @Transient
    public List<String> getImages() {
        List<String> urls = new ArrayList<>();
        for (ReviewImage image : reviewImages) {
            urls.add(image.getImageUrl());
        }
        return urls;
    }

    // Thiết lập lại danh sách ảnh
    @Transient
    public void setImages(List<String> imageUrls) {
        this.reviewImages.clear();
        for (String url : imageUrls) {
            ReviewImage image = new ReviewImage();
            image.setImageUrl(url);
            image.setReview(this);
            this.reviewImages.add(image);
        }
    }
}
