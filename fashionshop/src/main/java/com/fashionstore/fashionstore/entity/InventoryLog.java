package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    @JsonIgnore
    private ProductDetail productDetail;

    @Column(length = 50, nullable = false)
    private String action; // "IMPORT" hoặc "EXPORT"

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reference_type", length = 50)

    private String referenceType; // "ImportInvoice"

    @Column(name = "reference_id")
    private Integer referenceId; // Id của phiếu nhập/ xuất

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Validate quantity
    @PrePersist
    @PreUpdate
    private void validateQuantity() {
        if (quantity == null || quantity == 0) {
            throw new IllegalArgumentException("Quantity cannot be 0");
        }
        if (!"IMPORT".equals(action) && !"EXPORT".equals(action)) {
            throw new IllegalArgumentException("Action must be IMPORT or EXPORT");
        }
        if ("IMPORT".equals(action) && quantity < 0) {
            throw new IllegalArgumentException("Import quantity must be positive");
        }
        if ("EXPORT".equals(action) && quantity < 0) {
            throw new IllegalArgumentException("Export quantity must be positive");
            // hoặc bạn có thể cho phép âm để trừ kho
        }
    }

    // Truyền productDetailId cho JSON
    @Transient
    @JsonProperty("productDetailId")
    public Integer getProductDetailId() {
        return productDetail != null ? productDetail.getId() : null;
    }

}

