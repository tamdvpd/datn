package com.fashionstore.fashionstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ImportInvoiceDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportInvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nhiều - một với ImportInvoice
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_invoice_id", nullable = false)
    @JsonIgnoreProperties({ "importInvoiceDetails", "hibernateLazyInitializer", "handler" })
    private ImportInvoice importInvoice;

    // Nhiều - một với ProductDetail
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    @NotNull(message = "Chi tiết sản phẩm không được để trống")
    private ProductDetail productDetail;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải >= 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Đơn giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Đơn giá phải > 0")
    @Digits(integer = 13, fraction = 2, message = "Đơn giá không hợp lệ")
    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({ "importInvoiceDetails", "hibernateLazyInitializer", "handler" })
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ------------------ TRANSIENT FIELD ------------------
    @Column(name = "imported", nullable = false, columnDefinition = "BIT DEFAULT 0")
private boolean imported = false;

    // getter / setter cho imported
    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }
}
