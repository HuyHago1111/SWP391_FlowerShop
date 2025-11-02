package com.flowerShop1.repository;

import com.flowerShop1.dto.category.CategoryDTO;
import com.flowerShop1.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByCategoryName(String categoryName);
    List<Category> findByCategoryNameContainingIgnoreCase(String keyword);


    @Query(value = """
SELECT
    c.category_id,
    c.category_name,
    COUNT(p.product_id) AS ProductCount
FROM
    Categories c
LEFT JOIN
    Products p ON c.category_id = p.category_id
GROUP BY
    c.category_id,
    c.category_name
ORDER BY
    ProductCount DESC;

""" , nativeQuery = true)
    List<Object[]> findCategoryAll();
}
