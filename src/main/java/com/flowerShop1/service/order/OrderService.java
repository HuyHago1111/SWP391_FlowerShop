package com.flowerShop1.service.order;

import com.flowerShop1.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getOrdersByUserId(int userId);

}
