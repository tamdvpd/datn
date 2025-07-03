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
@Table(name = "Suppliers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
})
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    @Column(nullable = false, length = 100)
    private String name;

    @Email(message = "Email không đúng định dạng")
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String email;

    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số")
    @Column(name = "phone_number", length = 15, unique = true)
    private String phoneNumber;

    @Size(max = 255)
    @Column(length = 255)
    private String address;

    @NotNull
    @Column(nullable = false, columnDefinition = "BIT DEFAULT 1")
    private Boolean status = true; // true = hoạt động, false = ngừng hoạt động

    @Column(name = "created_at")
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
