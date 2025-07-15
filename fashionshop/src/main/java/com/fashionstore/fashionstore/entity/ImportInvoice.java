package com.fashionstore.fashionstore.entity;

<<<<<<< Updated upstream
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

=======
>>>>>>> Stashed changes
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

<<<<<<< Updated upstream
=======
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

>>>>>>> Stashed changes
@Entity
@Table(name = "ImportInvoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
<<<<<<< Updated upstream
public class ImportInvoice {

=======

public class ImportInvoice {
>>>>>>> Stashed changes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

<<<<<<< Updated upstream
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
=======
    // Quan hệ Nhiều - Một với Supplier
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

>>>>>>> Stashed changes
    @Column(name = "import_date", nullable = false)
    private LocalDate importDate;

    @Column(length = 255)
    private String note;

<<<<<<< Updated upstream
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
=======
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "importInvoice", cascade = CascadeType.ALL)
>>>>>>> Stashed changes
    private List<ImportInvoiceDetail> importInvoiceDetails = new ArrayList<>();
}
