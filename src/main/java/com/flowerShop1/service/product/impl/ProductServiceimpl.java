package com.flowerShop1.service.product.impl;

import com.flowerShop1.dto.product.ProductDTO;
import com.flowerShop1.dto.product.ProductOrderDTO;

import com.flowerShop1.config.ProductSpecifications;
import com.flowerShop1.entity.Product;
import com.flowerShop1.repository.ProductsRepository;
import com.flowerShop1.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceimpl implements ProductService {
    @Value("${upload.path:src/main/resources/static/admin-assets/images/products/}")
    private String imageFolder;

    // private final String imageFolder =
    // "src/main/resources/static/admin-assets/images/products/";
    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public List<Product> getAllProduct() {
        return productsRepository.findAll();
    }

    @Override
    public Product getProductById(int productId) {
        Product product = productsRepository.findByProductId(productId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        return product;
    }

    @Override
    public Page<ProductOrderDTO> getProductsOfOrderByUserId(int userId, Pageable pageable) {
        Page<Object[]> results = productsRepository.findProductsOfOrderByUserId(userId, pageable);
        return results.map(item -> new ProductOrderDTO(
                (int) item[0],
                (String) item[1],
                (String) item[2],
                (int) item[3],
                (BigDecimal) item[4],
                (java.sql.Timestamp) item[5],
                (String) item[6]

        ));

    }

    @Override
    public Product save(Product product) {
        productsRepository.save(product);
        return product;
    }

    @Override
    public Page<ProductDTO> getProductsByManyFields(String searchName, String categoryIDs, BigDecimal minPrice,
            BigDecimal maxPrice, String sortBy, Pageable pageable) {
        Page<Object[]> results = productsRepository.findProductsByManyFields(searchName, categoryIDs, minPrice,
                maxPrice, sortBy, pageable);
        return results.map(item -> new ProductDTO(
                (int) item[0],
                (String) item[1],
                ((BigDecimal) item[2]).doubleValue(),
                (int) item[3],
                (String) item[4],
                (String) item[5]));
    }

    @Override
    public Page<Product> searchProducts(String keyword, Integer categoryId, Integer supplierId,
            String sortBy, String sortDir, Pageable pageable) {
        Specification<Product> spec = Specification.allOf(
                ProductSpecifications.hasNameLike(keyword),
                ProductSpecifications.hasCategory(categoryId),
                ProductSpecifications.hasSupplier(supplierId));

        Sort sort = Sort.by(sortBy == null ? "createdAt" : sortBy);
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        Pageable sortedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return productsRepository.findAll(spec, sortedPage);
    }

    @Override
    public Optional<Product> getById(Integer id) {
        return productsRepository.findById(id);
    }

    @Override
    @Transactional

    public Product save(Product product, MultipartFile imageFile) throws IOException {
        // 🟢 Kiểm tra file upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String originalFilename = imageFile.getOriginalFilename();

            // 🔒 Đảm bảo tên file không null và hợp lệ
            if (originalFilename != null && !originalFilename.trim().isEmpty()) {
                String cleanFileName = System.currentTimeMillis() + "-" + StringUtils.cleanPath(originalFilename);
                Path uploadPath = Paths.get(imageFolder);

                // 🔧 Đảm bảo thư mục tồn tại
                if (Files.notExists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 🔄 Copy file, ghi đè nếu trùng tên
                Path filePath = uploadPath.resolve(cleanFileName);
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    product.setImageUrl("/admin-assets/images/products/" + cleanFileName);
                } catch (IOException e) {
                    throw new IOException("❌ Failed to store image file: " + cleanFileName, e);
                }
            }
        }

        // 🟠 Cập nhật trạng thái tự động dựa trên tồn kho
        updateStatusBasedOnStock(product);

        // 🕒 Set thời gian tạo / cập nhật
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        product.setUpdatedAt(LocalDateTime.now());

        // 💾 Lưu sản phẩm
        return productsRepository.save(product);
    }

    @Override
    public void deleteById(Integer id) {
        productsRepository.deleteById(id);
    }

    @Override
    public void updateStatusBasedOnStock(Product product) {
        if (product.getStockQuantity() == null || product.getStockQuantity() <= 0) {
            product.setStatus("Out of Stock");
        } else if ("Out of Stock".equalsIgnoreCase(product.getStatus())) {
            product.setStatus("Active");
        }
    }

    @Override
    public List<Product> findRelatedProducts(int categoryId, int productId, int limit) {
        Pageable pageable = PageRequest.of(0, limit); // Lấy trang đầu tiên, giới hạn số lượng
        return productsRepository.findRelatedProducts(categoryId, productId, pageable);
    }

    @Override
    public List<Product> findTrendingProducts(int limit) {
        return productsRepository.findTop4ByStatusOrderByUpdatedAtDesc("Active");
    }

}