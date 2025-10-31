package com.flowerShop1.service.category.impl;

import com.flowerShop1.dto.category.CategoryDTO;
import com.flowerShop1.entity.Category;
import com.flowerShop1.repository.CategoryRepository;
import com.flowerShop1.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
