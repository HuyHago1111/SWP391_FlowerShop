package com.flowerShop1.service.product.impl;

import com.flowerShop1.entity.Product;
import com.flowerShop1.repository.ProductsRepository;
import com.flowerShop1.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceimpl  implements ProductService {
    @Autowired
    private ProductsRepository productsRepository;
    @Override
    public List<Product> getAllProduct() {
        return productsRepository.findAll();
    }
}