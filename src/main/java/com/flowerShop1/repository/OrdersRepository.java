package com.flowerShop1.repository;

import com.flowerShop1.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository  extends JpaRepository<Orders, Integer> {
}
