package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonIgnore
    private ProductDetail productDetail;

    @Column(length = 50, nullable = false)
    private String action;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    private void validateQuantity() {
        if (quantity == 0) {
            throw new IllegalArgumentException("Quantity cannot be 0");
        }
}
@Transient
@JsonProperty("productDetailId") // 👈 Rất quan trọng
public Integer getProductDetailId() {
    return productDetail != null ? productDetail.getId() : null;
}

}
