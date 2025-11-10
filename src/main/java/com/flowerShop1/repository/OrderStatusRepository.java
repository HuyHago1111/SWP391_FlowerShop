package com.flowerShop1.repository;

import com.flowerShop1.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusRepository  extends JpaRepository<OrderStatus, Integer> {
    List<OrderStatus> findBystatusIdBetween(int start, int end);
}
