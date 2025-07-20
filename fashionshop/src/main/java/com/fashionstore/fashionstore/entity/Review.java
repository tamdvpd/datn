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

    // Liên kết đến bảng Users
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "reviews", "password", "orders", "wishlist", "importInvoices" }) // tuỳ chỉnh nếu có vòng
                                                                                             // lặp
    private User user;

    // Liên kết đến bảng ProductDetails
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    @JsonIgnoreProperties({ "reviews", "product", "importInvoiceDetails", "orderDetails" })
    private ProductDetail productDetail;

    // Điểm đánh giá: từ 1 đến 5
    @Column(nullable = false)
    private Integer rating;

    // Nội dung đánh giá (tùy chọn)
    @Column(length = 1000)
    private String comment;

    // Thời gian tạo
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Thời gian cập nhật
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Danh sách ảnh kèm theo (nếu có)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

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
