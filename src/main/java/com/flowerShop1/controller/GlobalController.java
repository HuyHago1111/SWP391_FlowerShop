package com.flowerShop1.controller;

import com.flowerShop1.entity.Category;
import com.flowerShop1.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalController {
    @Autowired
    private CategoryService categoryService;
     @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategory().stream().limit(9).toList();
    }
}
