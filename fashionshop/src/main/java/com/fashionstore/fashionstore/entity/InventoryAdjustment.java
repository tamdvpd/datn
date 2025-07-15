package com.fashionstore.fashionstore.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
<<<<<<< Updated upstream
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
=======
import jakarta.persistence.Table;
>>>>>>> Stashed changes
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "InventoryAdjustments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

<<<<<<< Updated upstream
    @NotBlank(message = "Lý do không được để trống!")
    @Size(max = 100, message = "Nội dung tối đa là 100 ký tự!")
    @Column(nullable = false, length = 100)
    private String reason; // Lý do điều chỉnh tồn kho

    @Size(max = 225, message = "Ghi chú tối đa là 255 ký tự!")
=======
    @Column(nullable = false, length = 100)
    private String reason; // Lý do điều chỉnh tồn kho

>>>>>>> Stashed changes
    @Column(length = 255)
    private String note; // Ghi chú (nếu có)

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

<<<<<<< Updated upstream
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
=======
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
>>>>>>> Stashed changes
    }

    @OneToMany(mappedBy = "adjustment", cascade = CascadeType.ALL)
    private List<InventoryAdjustmentDetail> inventoryAdjustmentDetails = new ArrayList<>();
}
