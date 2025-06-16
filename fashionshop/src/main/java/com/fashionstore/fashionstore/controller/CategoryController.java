package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Category;
import com.fashionstore.fashionstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
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

    // Tạo danh mục mới với @Valid
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @Valid @RequestBody Category category) {
        Optional<Category> existing = categoryService.getCategoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category updated = categoryService.updateCategory(id, category);
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