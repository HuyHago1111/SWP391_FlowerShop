package com.flowerShop1.service.product;

import com.flowerShop1.dto.product.ProductOrderDTO;
import com.flowerShop1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();
    Product getProductById(int productId);
    Page<ProductOrderDTO> getProductsOfOrderByUserId(int userId, Pageable pageable);
    Product save(Product product);
}
