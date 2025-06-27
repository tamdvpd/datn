package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "InventoryLogs")
public class InventoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    @Column(length = 50, nullable = false)
    private String action;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reference_type", length = 50)
    private String referenceType; // ImportInvoice, Order, Adjustment

    @Column(name = "reference_id")
    private Integer referenceId; // ID của chứng từ gốc (không FK)

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}