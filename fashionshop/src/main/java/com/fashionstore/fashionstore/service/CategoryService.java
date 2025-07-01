package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategorys();

    Page<Category> getAllCategorys(Pageable pageable);

    Optional<Category> getCategoryById(Integer id);

    Category createCategory(Category category, MultipartFile imageFile);

    Category updateCategory(Integer id, Category category);

    void deleteCategory(Integer id);
}