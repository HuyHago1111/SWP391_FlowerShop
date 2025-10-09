package com.flowerShop1.service.category.impl;

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
}
