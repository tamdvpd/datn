package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ShippingLogs")
public class ShippingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @Column(length=50)
    private String shippingProvider;

    @Column(length=100)
    private String trackingCode;

    @Column(precision=10, scale=2)
    private BigDecimal shippingFee;

    @Column(length=50)
    private String status;

    private LocalDateTime updatedAt;
}