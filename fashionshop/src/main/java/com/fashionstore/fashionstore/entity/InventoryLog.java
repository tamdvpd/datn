package com.fashionstore.fashionstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "InventoryLogs") 
public class InventoryLog {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "product_detail_id", nullable = false, 
            foreignKey = @ForeignKey(name = "fk_inventory_product_detail")) 
    private ProductDetail productDetail; 

    @Column(length = 50, nullable = false) 
    private String action; 

    @Column(nullable = false) 
    private Integer quantity; 

    @Column(name = "reference_type", length = 50) 
    private String referenceType; 

    @Column(name = "reference_id") 
    private Integer referenceId; 

    @CreationTimestamp 
    @Column(name = "created_at", updatable = false) 
    private LocalDateTime createdAt;
}