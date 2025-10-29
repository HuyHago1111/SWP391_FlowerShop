package com.flowerShop1.service.category;

import com.flowerShop1.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategory();
    Page<Category> getAllCategories(String keyword, Pageable pageable);
    Optional<Category> getById(Integer id);
    Category save(Category category);
    void deleteById(Integer id);
}
