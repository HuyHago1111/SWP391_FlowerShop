package com.flowerShop1.service.product;

import com.flowerShop1.dto.product.ProductDTO;
import com.flowerShop1.dto.product.ProductOrderDTO;
import com.flowerShop1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;
@Service
public interface ProductService {
    List<Product> getAllProduct();
    Product getProductById(int productId);
    Page<ProductOrderDTO> getProductsOfOrderByUserId(int userId, Pageable pageable);
    Product save(Product product);

    Page<ProductDTO> getProductsByManyFields(String searchName, String categoryIDs, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, Pageable pageable );

    Page<Product> searchProducts(String keyword, Integer categoryId, Integer supplierId,
                                 String sortBy, String sortDir, Pageable pageable);

    Optional<Product> getById(Integer id);

    Product save(Product product, MultipartFile imageFile) throws IOException;

    void deleteById(Integer id);

    void updateStatusBasedOnStock(Product product);
}
