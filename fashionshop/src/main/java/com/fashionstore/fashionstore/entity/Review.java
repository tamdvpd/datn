package com.fashionstore.fashionstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "reviews", "password", "orders", "wishlist", "importInvoices" })
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    
    @JoinColumn(name = "product_detail_id", nullable = false)
    @JsonIgnoreProperties({ "reviews", "product", "importInvoiceDetails", "orderDetails" })
    private ProductDetail productDetail;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 1000)
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Transient
    public List<String> getImages() {
        List<String> urls = new ArrayList<>();
        for (ReviewImage image : reviewImages) {
            urls.add(image.getImageUrl());
        }
        return urls;
    }

    @Transient
    public void setImages(List<String> imageUrls) {
        this.reviewImages.clear(); // Xóa ảnh cũ nếu có

        for (String url : imageUrls) {
            ReviewImage image = new ReviewImage();
            image.setImageUrl(url);
            image.setReview(this); // Thiết lập quan hệ ngược
            this.reviewImages.add(image);
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
