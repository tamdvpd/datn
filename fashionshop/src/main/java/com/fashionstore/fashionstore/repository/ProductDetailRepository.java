package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
     List<ProductDetail> findByProductId(Integer productId);

    @Query("SELECT DISTINCT pd.product FROM ProductDetail pd WHERE " +
           "(:name IS NULL OR LOWER(pd.product.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:categoryId IS NULL OR pd.product.category.id = :categoryId) AND " +
           "(:minPrice IS NULL OR pd.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR pd.price <= :maxPrice)")
    static
    List<Product> searchProducts(
        @Param("name") String name,
        @Param("categoryId") Integer categoryId,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    ) {
        throw new UnsupportedOperationException("Unimplemented method 'searchProducts'");
    }
    
    public static ProductDetailBuilder builder() {
        return new ProductDetailBuilder();
    }

    public static class ProductDetailBuilder {
        private Integer id;
        private Product product;
        private String color;
        private String size;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal discountPrice;
        private String imageUrl;
        private BigDecimal weight;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ProductDetailBuilder id(Integer id) {
            this.id = id;
            return this;
        }
        public ProductDetailBuilder product(Product product) {
            this.product = product;
            return this;
        }
        public ProductDetailBuilder color(String color) {
            this.color = color;
            return this;
        }
        public ProductDetailBuilder size(String size) {
            this.size = size;
            return this;
        }
        public ProductDetailBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }
        public ProductDetailBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }
        public ProductDetailBuilder discountPrice(BigDecimal discountPrice) {
            this.discountPrice = discountPrice;
            return this;
        }
        public ProductDetailBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }
        public ProductDetailBuilder weight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }
        public ProductDetailBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public ProductDetailBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        
    }
}