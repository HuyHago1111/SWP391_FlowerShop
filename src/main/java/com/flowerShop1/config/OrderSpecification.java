package com.flowerShop1.config;

import com.flowerShop1.entity.Order;
import org.springframework.data.jpa.domain.Specification;
import com.flowerShop1.entity.Payment;
import java.time.LocalDateTime;

public class OrderSpecification {

    public static Specification<Order> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return null;
            return cb.like(cb.lower(root.get("user").get("fullName")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Order> hasPayment(String paymentMethod) {
        return (root, query, cb) -> {
            if (paymentMethod == null || paymentMethod.equalsIgnoreCase("all")) return null;

            // Tạo subquery để lấy order_id từ bảng payments có payment_method trùng khớp
            var subquery = query.subquery(Integer.class);
            var paymentRoot = subquery.from(com.flowerShop1.entity.Payment.class);
            subquery.select(paymentRoot.get("orderId"))
                    .where(cb.equal(cb.lower(paymentRoot.get("payment_method")), paymentMethod.toLowerCase()));

            // Điều kiện: orderId của Order nằm trong danh sách order_id vừa tìm được
            return cb.in(root.get("orderId")).value(subquery);
        };
    }


    public static Specification<Order> hasStatus(Integer statusId) {
        return (root, query, cb) -> {
            if (statusId == null) return null;
            return cb.equal(root.get("orderStatus").get("statusId"), statusId);
        };
    }

    public static Specification<Order> hasDateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            if (from != null && to != null) return cb.between(root.get("orderDate"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("orderDate"), from);
            return cb.lessThanOrEqualTo(root.get("orderDate"), to);
        };
    }

    public static Specification<Order> hasTotalBetween(Double minTotal, Double maxTotal) {
        return (root, query, cb) -> {
            if (minTotal == null && maxTotal == null) return null;
            if (minTotal != null && maxTotal != null) return cb.between(root.get("totalAmount"), minTotal, maxTotal);
            if (minTotal != null) return cb.greaterThanOrEqualTo(root.get("totalAmount"), minTotal);
            return cb.lessThanOrEqualTo(root.get("totalAmount"), maxTotal);
        };
    }
}
