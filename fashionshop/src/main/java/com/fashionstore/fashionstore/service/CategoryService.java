package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Integer id);

    Category createCategory(Category category, MultipartFile imageFile);

    Category updateCategory(Category category, MultipartFile imageFile);

    void deleteCategory(Integer id);
}
