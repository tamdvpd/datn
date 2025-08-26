package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "products")
@EqualsAndHashCode(exclude = "products")
@Entity
@Table(name = "Categories",
       uniqueConstraints = @UniqueConstraint(name = "UQ_Categories_Name", columnNames = "name"))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 100, message = "Tên danh mục tối đa 100 ký tự")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 255, message = "Mô tả tối đa 255 ký tự")
    @Column(length = 255)
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @NotNull
    @Column(nullable = false)
    private Boolean status = true; // Mặc định hiển thị (true)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Quan hệ 1-nhiều với Product
    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
}
