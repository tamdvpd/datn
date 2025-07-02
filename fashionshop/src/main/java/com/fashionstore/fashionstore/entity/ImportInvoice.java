package com.fashionstore.fashionstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ImportInvoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ✅ Quan hệ nhiều - một với Supplier
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    @NotNull(message = "Nhà cung cấp không được để trống")
    @JsonIgnoreProperties({ "importInvoices", "hibernateLazyInitializer", "handler" }) // ✅ Tránh vòng lặp JSON
    private Supplier supplier;

    @DecimalMin(value = "0.0", inclusive = true, message = "Tổng tiền phải >= 0")
    @Digits(integer = 13, fraction = 2, message = "Tổng tiền không hợp lệ")
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull(message = "Ngày nhập không được để trống")
    @Column(name = "import_date", nullable = false)
    private LocalDate importDate;

    @Column(length = 255)
    private String note;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.importDate == null) {
            this.importDate = LocalDate.now(); // ✅ Gán mặc định ngày nhập là ngày hiện tại
        }
    }

    // ✅ Một phiếu nhập có nhiều dòng chi tiết
    @OneToMany(mappedBy = "importInvoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportInvoiceDetail> importInvoiceDetails = new ArrayList<>();
}
