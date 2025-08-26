package com.fashionstore.fashionstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên nhà cung cấp không được để trống.")
    @Size(max = 100, message = "Tên nhà cung cấp không được vượt quá 100 ký tự.")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email không được để trống.")
    @Email(message = "Email không đúng định dạng.")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự.")
    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không đúng định dạng (bắt đầu bằng 0 và đủ 10 số).")
    @Column(name = "phone_number", length = 15, unique = true, nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống.")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự.")
    @Column(length = 255, nullable = false)
    private String address;

    @Builder.Default
    @Column(name = "status", nullable = false) // map đúng với cột status trong DB
    private Boolean status = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ImportInvoice> importInvoices = new ArrayList<>();
}