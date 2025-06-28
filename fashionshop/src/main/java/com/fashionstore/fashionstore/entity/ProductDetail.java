package com.fashionstore.fashionstore.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ProductDetails")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 50)
    private String color;

    @Column(length = 50)
    private String size;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "discount_price", precision = 12, scale = 2)
    private BigDecimal discountPrice;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    private List<ImportInvoiceDetail> importInvoiceDetails = new ArrayList<>();

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    private List<InventoryAdjustmentDetail> inventoryAdjustmentDetails = new ArrayList<>();

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    private List<InventoryLog> inventoryLogs = new ArrayList<>();

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
}