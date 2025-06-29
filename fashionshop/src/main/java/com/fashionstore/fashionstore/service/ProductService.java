package com.fashionstore.fashionstore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fashionstore.fashionstore.entity.Product;

public interface ProductService {
    List<Product> getAllProducts();

    Page<Product> getAllProducts(Pageable pageable);

    Optional<Product> getProductById(Integer id);

    Product createProduct(Product product);

    Product updateProduct(Integer id, Product product);

    void deleteProduct(Integer id);

    List<Product> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);

}