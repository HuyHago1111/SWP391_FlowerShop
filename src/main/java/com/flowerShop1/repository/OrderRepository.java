package com.flowerShop1.repository;

import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByOrderDateDesc(User user);


    List<Order> findByUserUserId(int UserId);
}
