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

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Column(length=10, nullable=false)
    private String action;

    @Column(nullable=false)
    private Integer quantity;

    @Column(columnDefinition="TEXT")
    private String note;

    private LocalDateTime createdAt;
}