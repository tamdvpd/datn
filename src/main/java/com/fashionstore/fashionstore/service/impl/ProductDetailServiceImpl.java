package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    @Override
    public List<ProductDetail> getAllProductDetails() {
        return productDetailRepository.findAll();
    }

    @Override
    public Page<ProductDetail> getAllProductDetails(Pageable pageable) {
        return productDetailRepository.findAll(pageable);
    }

    @Override
    public Optional<ProductDetail> getProductDetailById(Integer id) {
        return productDetailRepository.findById(id);
    }

    @Override
    public ProductDetail createProductDetail(ProductDetail productDetail) {
        return productDetailRepository.save(productDetail);
    }

    @Override
    public ProductDetail updateProductDetail(Integer id, ProductDetail productDetail) {
        productDetail.setId(id);
        return productDetailRepository.save(productDetail);
    }

    @Override
    public void deleteProductDetail(Integer id) {
        productDetailRepository.deleteById(id);
    }
}