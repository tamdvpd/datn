package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "OrderStatusHistory")
public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @Column(length=20, nullable=false)
    private String status;

    private LocalDateTime updatedAt;
}