package com.flowerShop1.config;

import com.flowerShop1.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> hasNameLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return null;
            return cb.like(cb.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasCategory(Integer categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;
            return cb.equal(root.get("category").get("categoryId"), categoryId);
        };
    }

    public static Specification<Product> hasSupplier(Integer supplierId) {
        return (root, query, cb) -> {
            if (supplierId == null) return null;
            return cb.equal(root.get("supplier").get("supplierId"), supplierId);
        };
    }
}
