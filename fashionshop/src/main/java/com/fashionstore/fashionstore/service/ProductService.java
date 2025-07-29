package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Page<Product> getAllProducts(Pageable pageable);
    Optional<Product> getProductById(Integer id);
    Product createProduct(Product product);
    Product updateProduct(Integer id, Product product);
    void deleteProduct(Integer id); // Xóa mềm (status=false)
    List<Product> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);
}
