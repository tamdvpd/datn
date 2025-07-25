package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.repository.ProductRepository;
import com.fashionstore.fashionstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        // Lấy tất cả sản phẩm đang bán (status = true)
        return productRepository.findAll()
                .stream()
                .filter(Product::getStatus)
                .toList();
    }

    @Override
public Page<Product> getAllProducts(Pageable pageable) {
    return productRepository.findByStatusTrue(pageable);
}


    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id)
                .filter(Product::getStatus); // Chỉ trả về sản phẩm đang bán
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus(true); // Sản phẩm mới luôn active
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Integer id, Product product) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setDescription(product.getDescription());
                    existing.setBrand(product.getBrand());
                    existing.setImageUrl(product.getImageUrl());
                    existing.setStatus(product.getStatus());
                    existing.setCategory(product.getCategory());
                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setStatus(false); // Xóa mềm
            productRepository.save(product);
        });
    }

    @Override
    public List<Product> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.searchProducts(name, categoryId, minPrice, maxPrice)
                .stream()
                .filter(Product::getStatus) // Chỉ trả sản phẩm đang bán
                .toList();
    }
}
