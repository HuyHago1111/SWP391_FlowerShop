package com.flowerShop1.service.category.impl;

import com.flowerShop1.dto.category.CategoryDTO;
import com.flowerShop1.entity.Category;
import com.flowerShop1.repository.CategoryRepository;
import com.flowerShop1.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;
@Service
public class CategoryServiceimpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }


    @Override
    public List<CategoryDTO> getAllCategoriesWithProductCount() {
        List<Object[]> results = categoryRepository.findCategoryAll();
        return results.stream()
                .map(objects -> new CategoryDTO((int) objects[0], (String) objects[1], (int) objects[2]))
                .toList();
    }

    @Override
    public Page<Category> getAllCategories(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            List<Category> filtered = categoryRepository.findByCategoryNameContainingIgnoreCase(keyword);
            return new PageImpl<>(filtered, pageable, filtered.size());
        } else {
            return categoryRepository.findAll(pageable);
        }
    }

    @Override
    public Optional<Category> getById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }
}
