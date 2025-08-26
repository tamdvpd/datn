package com.fashionstore.fashionstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    @NotNull(message = "Nhà cung cấp không được để trống")
    @JsonIgnoreProperties({ "importInvoices", "hibernateLazyInitializer", "handler" })
    private Supplier supplier;

    @NotNull(message = "Ngày nhập không được để trống")
    @Column(name = "import_date", nullable = false)
    private LocalDate importDate;

    @Column(length = 255)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "importInvoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ImportInvoiceDetail> importInvoiceDetails = new ArrayList<>();

    public enum Status {
        PENDING, // Chưa nhập kho
        COMPLETED // Đã nhập kho
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.importDate == null) {
            this.importDate = LocalDate.now();
        }
    }
}
