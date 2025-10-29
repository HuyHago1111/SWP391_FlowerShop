package com.flowerShop1.repository;

import com.flowerShop1.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository  extends JpaRepository<OrderStatus, Integer> {
}
