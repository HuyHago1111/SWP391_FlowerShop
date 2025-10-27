package com.flowerShop1.repository;

import com.flowerShop1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductsRepository  extends JpaRepository <Product, Integer>, JpaSpecificationExecutor<Product> {
    Product findByProductId(int productId);
    boolean existsByProductName(String productName);
}
