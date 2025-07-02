package com.fashionstore.fashionstore.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ShippingProviders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ShippingProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên đơn vị không được để trống")
    @Size(max = 100, message = "Tên đơn vị tối đa 100 ký tự")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Mã code không được để trống")
    @Size(max = 50, message = "Mã code tối đa 50 ký tự")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Mã code chỉ được chứa chữ in hoa và số")
    @Column(nullable = false, length = 50, unique = true)
    private String code;

    @Size(max = 255, message = "Mô tả tối đa 255 ký tự")
    @Column(length = 255)
    private String description;

    @NotNull(message = "Phí giao hàng không được để trống")
    @DecimalMin(value = "0.00", message = "Phí giao hàng không được nhỏ hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Phí giao hàng quá lớn hoặc sai định dạng (tối đa 10 số và 2 số sau dấu phẩy)")
    @Column(name = "shipping_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal shippingFee;

    @NotNull(message = "Trạng thái không được để trống")
    @Column(nullable = false)
    private Boolean status = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.shippingFee == null) {
            this.shippingFee = BigDecimal.ZERO;
        }
        if (this.status == null) {
            this.status = true;
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
