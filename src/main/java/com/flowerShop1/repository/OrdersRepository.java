package com.flowerShop1.repository;

import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface OrdersRepository  extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByOrderDateDesc(User user);
}
