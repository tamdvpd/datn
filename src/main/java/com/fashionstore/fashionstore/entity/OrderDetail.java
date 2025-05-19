package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "OrderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_detail_id", nullable=false)
    private ProductDetail productDetail;

    @Column(nullable=false)
    private Integer quantity;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal price;
}