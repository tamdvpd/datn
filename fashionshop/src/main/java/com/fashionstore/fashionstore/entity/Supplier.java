package com.fashionstore.fashionstore.entity;

<<<<<<< Updated upstream
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

=======
>>>>>>> Stashed changes
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

<<<<<<< Updated upstream
@Entity
=======
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Suppliers")
>>>>>>> Stashed changes
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
<<<<<<< Updated upstream
@Table(name = "Suppliers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
})
public class Supplier {

=======
public class Supplier {
>>>>>>> Stashed changes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

<<<<<<< Updated upstream
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
=======
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private Boolean status = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
>>>>>>> Stashed changes
    }

    @PreUpdate
    protected void onUpdate() {
<<<<<<< Updated upstream
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
=======
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
>>>>>>> Stashed changes
    private List<ImportInvoice> importInvoices = new ArrayList<>();
}
