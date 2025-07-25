package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Category;
import com.fashionstore.fashionstore.service.CategoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Lấy tất cả danh mục
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategorys());
    }

    // Lấy danh mục theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Thêm mới danh mục
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Category> createCategory(@RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setStatus(status != null ? status : true);
        Category created = categoryService.createCategory(category, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Cập nhật danh mục
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        Optional<Category> existing = categoryService.getCategoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category category = existing.get();
        category.setName(name);
        category.setDescription(description);
        category.setStatus(status != null ? status : true);

        Category updated = categoryService.createCategory(category, imageFile); // dùng chung createCategory để lưu ảnh
        return ResponseEntity.ok(updated);
    }

    // Xoá danh mục
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        Optional<Category> existing = categoryService.getCategoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
