package com.fashionstore.fashionstore.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.service.ProductDetailService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/productDetails")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getById(@PathVariable Integer id) {
        Optional<ProductDetail> productDetail = productDetailService.getProductDetailById(id);
        return productDetail.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}