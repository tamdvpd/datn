package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable=false, length=50)
    private String code;

    @Column(precision=5, scale=2)
    private BigDecimal discountPercent;

    private LocalDate expiryDate;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}