package com.fashionstore.fashionstore.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ProductDetails")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties("productDetails")
    @NotNull(message = "Sản phẩm không được null")
    private Product product;

    @Column(length = 50)
    @NotBlank(message = "Màu sắc không được để trống")
    @Size(max = 50, message = "Màu sắc tối đa 50 ký tự")
    private String color;

    @Column(length = 50)
    @NotBlank(message = "Kích thước không được để trống")
    @Size(max = 50, message = "Kích thước tối đa 50 ký tự")
    private String size;

    @Column(nullable = false)
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải >= 0")
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá sản phẩm phải lớn hơn 0")
    private BigDecimal price;

    @Column(name = "discount_price", precision = 12, scale = 2)
    @DecimalMin(value = "0.0", message = "Giá giảm phải >= 0")
    private BigDecimal discountPrice;

    @Column(name = "image_url", length = 255)
    @Size(max = 255, message = "Đường dẫn ảnh quá dài (tối đa 255 ký tự)")
    private String imageUrl;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "Trọng lượng phải >= 0")
    private BigDecimal weight;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ✅ Tự động cập nhật thời gian
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Các quan hệ khác
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

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("productDetail")
    private List<Review> reviews = new ArrayList<>();
}