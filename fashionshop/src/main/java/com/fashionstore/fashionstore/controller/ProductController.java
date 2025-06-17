package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Lấy tất cả sản phẩm
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id) {
        return ResponseEntity.of(productService.getProductById(id));
    }

    // Tạo sản phẩm mới
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Integer id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Tìm kiếm sản phẩm theo tên, danh mục, khoảng giá
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.searchProducts(name, categoryId, minPrice, maxPrice));
    }

    // 🔥 API: Sản phẩm bán chạy (random 4 sản phẩm)
    @GetMapping("/hot")
    public ResponseEntity<List<Product>> getHotProducts() {
        List<Product> all = productService.getAllProducts();
        Collections.shuffle(all);
        return ResponseEntity.ok(all.stream().limit(4).toList());
    }

    // 💸 API: Sản phẩm đang sale (random 4 sản phẩm khác)
    @GetMapping("/sale")
    public ResponseEntity<List<Product>> getSaleProducts() {
        List<Product> all = productService.getAllProducts();
        Collections.shuffle(all);
        return ResponseEntity.ok(all.stream().skip(4).limit(4).toList());
    }

    @GetMapping("/with-price")
    public ResponseEntity<List<Map<String, Object>>> getProductsWithPrice() {
        return ResponseEntity.ok(productService.getProductsWithPrice());
    }

}
