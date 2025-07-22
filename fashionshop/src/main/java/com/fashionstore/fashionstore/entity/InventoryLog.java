package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "InventoryLogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_detail_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inventory_product_detail"))
    private ProductDetail productDetail;

    @Column(length = 50, nullable = false)
    private String action; // "IMPORT" hoặc "EXPORT"

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reference_type", length = 50)
    private String referenceType; // "IMPORT_INVOICE", "ORDER", etc.

    @Column(name = "reference_id")
    private Integer referenceId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
