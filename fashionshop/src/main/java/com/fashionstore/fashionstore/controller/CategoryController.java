package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Category;
import com.fashionstore.fashionstore.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    //  Lấy danh mục theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Thêm mới danh mục
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        if (category.getStatus() == null) {
            category.setStatus(true); // Mặc định là hiển thị
        }
        Category created = categoryService.createCategory(category);
        return ResponseEntity.status(201).body(created);
    }

    // Cập nhật danh mục
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @Valid @RequestBody Category category) {
        Optional<Category> existing = categoryService.getCategoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        category.setId(id); // Cập nhật đúng ID
        Category updated = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    //  Xoá danh mục
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        Optional<Category> existing = categoryService.getCategoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // Upload ảnh đại diện cho danh mục
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/categories"; 
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(dir, filename);
            file.transferTo(dest);

            String imageUrl = "/uploads/categories/" + filename; // nếu có cấu hình static
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Upload thất bại: " + e.getMessage());
        }
    }
}
