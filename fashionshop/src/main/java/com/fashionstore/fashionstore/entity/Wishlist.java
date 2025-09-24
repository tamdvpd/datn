package com.fashionstore.fashionstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
    name = "Wishlists",
    uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "product_id" })
)
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"wishlists", "orders", "reviews"}) // tránh vòng lặp JSON
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"wishlists", "orderDetails", "reviews"}) // tránh vòng lặp JSON
    private Product product;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // tự set thời điểm thêm
}
