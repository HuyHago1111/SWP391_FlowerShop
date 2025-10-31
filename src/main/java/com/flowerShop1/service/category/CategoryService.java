package com.flowerShop1.service.category;

import com.flowerShop1.dto.category.CategoryDTO;
import com.flowerShop1.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    List<CategoryDTO> getAllCategoriesWithProductCount();
}
