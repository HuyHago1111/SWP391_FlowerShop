package com.flowerShop1.repository;

import com.flowerShop1.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository  extends JpaRepository <Products, Integer> {
}
