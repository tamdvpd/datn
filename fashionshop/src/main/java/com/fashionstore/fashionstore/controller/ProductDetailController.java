package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productDetails")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @GetMapping
    public ResponseEntity<List<ProductDetail>> getAll() {
        return ResponseEntity.ok(productDetailService.getAllProductDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getById(@PathVariable Integer id) {
        return ResponseEntity.of(productDetailService.getProductDetailById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDetail> create(@RequestBody ProductDetail productDetail) {
        return ResponseEntity.ok(productDetailService.createProductDetail(productDetail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetail> update(@PathVariable Integer id, @RequestBody ProductDetail productDetail) {
        return ResponseEntity.ok(productDetailService.updateProductDetail(id, productDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productDetailService.deleteProductDetail(id);
        return ResponseEntity.noContent().build();
    }
}