package com.flowerShop1.repository;

import com.flowerShop1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository  extends JpaRepository <Product, Integer> {
    Product findByProductId(int productId);
}
