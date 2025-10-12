package com.flowerShop1.service.product;

import com.flowerShop1.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();
    Product getProductById(int productId);
}
