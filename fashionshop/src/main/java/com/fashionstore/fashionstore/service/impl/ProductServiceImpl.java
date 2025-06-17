package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.ProductRepository;
import com.fashionstore.fashionstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Integer id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        List<ProductDetail> details = productDetailRepository.searchProducts(name, categoryId, minPrice, maxPrice);
        return details.stream()
                .map(ProductDetail::getProduct)
                .distinct()
                .toList();
    }

    @Override
    public List<Map<String, Object>> getProductsWithPrice() {
        List<Product> products = productRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Product product : products) {
            Optional<ProductDetail> detailOpt = productDetailRepository.findFirstByProduct_Id(product.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("name", product.getName());
            map.put("imageUrl", product.getImageUrl());
            map.put("description", product.getDescription());

            BigDecimal price = detailOpt.map(ProductDetail::getPrice).orElse(BigDecimal.ZERO);
            map.put("price", price);

            result.add(map);
        }

        return result;
    }

}
