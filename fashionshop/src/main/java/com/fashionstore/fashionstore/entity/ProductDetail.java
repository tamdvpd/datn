package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ProductDetails")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 50)
    private String color;

    @Column(length = 50)
    private String size;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}