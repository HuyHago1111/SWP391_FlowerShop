package com.flowerShop1.service.order;

import com.flowerShop1.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    Page<Order> getOrdersByUserId(int userId, org.springframework.data.domain.Pageable pageable);
    Order save(Order order);

}
