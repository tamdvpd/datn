package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private LocalDateTime orderDate;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal totalAmount;

    @Column(length=20)
    private String status;

    @Column(columnDefinition="TEXT")
    private String shippingAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}