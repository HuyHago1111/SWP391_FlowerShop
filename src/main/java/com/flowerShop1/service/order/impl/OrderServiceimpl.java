package com.flowerShop1.service.order.impl;

import com.flowerShop1.entity.Order;
import com.flowerShop1.repository.OrderRepository;
import com.flowerShop1.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceimpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public Page<Order> getOrdersByUserId(int userId, org.springframework.data.domain.Pageable pageable) {
        return orderRepository.findByUserUserId(userId , pageable);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);

    }
}
