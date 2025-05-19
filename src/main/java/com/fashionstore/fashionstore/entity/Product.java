package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, length=100)
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition="TEXT")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}