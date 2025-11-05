package com.flowerShop1.repository;

import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> , JpaSpecificationExecutor<Order> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    Page<Order> findByUserUserId(int UserId, org.springframework.data.domain.Pageable pageable);

}
