package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Category;
import com.fashionstore.fashionstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.of(categoryService.getCategoryById(id));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Category>> getAllCategoriesPaged(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Category> createCategory(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        if (imageFile != null && !imageFile.isEmpty() && !isImage(imageFile)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setStatus(status != null ? status : true);

        Category created = categoryService.createCategory(category, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Category> updateCategory(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        Optional<Category> existing = categoryService.getCategoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (imageFile != null && !imageFile.isEmpty() && !isImage(imageFile)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        Category category = existing.get();
        category.setName(name);
        category.setDescription(description);
        category.setStatus(status != null ? status : true);

        Category updated = categoryService.updateCategory(category, imageFile);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        Optional<Category> existing = categoryService.getCategoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}