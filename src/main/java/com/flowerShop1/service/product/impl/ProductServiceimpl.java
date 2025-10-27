package com.flowerShop1.service.product.impl;

import com.flowerShop1.dto.product.ProductOrderDTO;
import com.flowerShop1.entity.Product;
import com.flowerShop1.repository.ProductsRepository;
import com.flowerShop1.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;



import java.math.BigDecimal;
import java.util.List;
@Service
public class ProductServiceimpl  implements ProductService {
    @Autowired
    private ProductsRepository productsRepository;
    @Override
    public List<Product> getAllProduct() {
        return productsRepository.findAll();
    }

    @Override
    public Product getProductById(int productId) {
        Product product = productsRepository.findByProductId(productId);
        if(product == null) {
            throw new RuntimeException("Product not found");
        }
        return product;
    }

    @Override
    public Page<ProductOrderDTO> getProductsOfOrderByUserId(int userId, Pageable pageable) {
        Page<Object[]> results = productsRepository.findProductsOfOrderByUserId(userId, pageable);
        return  results.map(item -> new ProductOrderDTO(
                (int) item[0],
                (String) item[1],
                (String) item[2],
                (int) item[3],
                (BigDecimal) item[4],
                (java.sql.Timestamp)item[5],
                (String) item[6]

        ));



    }
}