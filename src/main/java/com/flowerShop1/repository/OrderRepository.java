package com.flowerShop1.repository;

import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> , JpaSpecificationExecutor<Order> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    Page<Order> findByUserUserId(int UserId, org.springframework.data.domain.Pageable pageable);
    // ✅ JOIN FETCH user, orderStatus, paymentStatus để tránh N+1
    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderStatus " +
            "LEFT JOIN FETCH o.paymentStatus " +
            "WHERE o.orderId = :orderId")
    Optional<Order> findByIdWithRelations(Integer orderId);
    @EntityGraph(attributePaths = {"user", "orderStatus", "paymentStatus", "shipper"})
    Optional<Order> findByOrderId(Integer id);
}
