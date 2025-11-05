package com.flowerShop1.service.order.impl;

import com.flowerShop1.entity.Order;
import com.flowerShop1.repository.OrderRepository;
import com.flowerShop1.repository.OrderStatusRepository;
import com.flowerShop1.repository.ShipperRepository;
import com.flowerShop1.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.flowerShop1.entity.OrderStatus;
import com.flowerShop1.entity.Shipper;
import com.flowerShop1.config.OrderSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceimpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusRepository statusRepository;
    @Autowired
    private ShipperRepository shipperRepository;
    @Override
    public Page<Order> getOrdersByUserId(int userId, org.springframework.data.domain.Pageable pageable) {
        return orderRepository.findByUserUserId(userId , pageable);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    // Bổ sung các phương thức mới
    @Override
    public Page<Order> searchOrders(String keyword, String paymentMethod, Integer statusId,
                                    String sortBy, String sortDir,
                                    Pageable pageable,
                                    LocalDateTime fromDate, LocalDateTime toDate,
                                    Double minTotal, Double maxTotal) {

        Sort sort = Sort.by(sortBy);
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Specification<Order> spec = Specification.allOf(
                OrderSpecification.hasKeyword(keyword),
                OrderSpecification.hasPayment(paymentMethod),
                OrderSpecification.hasStatus(statusId),
                OrderSpecification.hasDateBetween(fromDate, toDate),
                OrderSpecification.hasTotalBetween(minTotal, maxTotal));

        return orderRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<Order> getById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Optional<OrderStatus> getStatusById(Integer statusId) {
        return statusRepository.findById(statusId);
    }

    @Override
    public List<OrderStatus> getAllStatuses() {
        return statusRepository.findAll(Sort.by("statusId"));
    }

    @Override
    public void updateOrderStatus(Integer orderId, Integer statusId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        OrderStatus orderstatus = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        order.setOrderStatus(orderstatus);
        orderRepository.save(order);
    }

    @Override
    public void assignShipper(Integer orderId, Integer shipperId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Shipper shipper = shipperRepository.findById(shipperId)
                .orElseThrow(() -> new RuntimeException("Shipper not found"));

        order.setShipper(shipper);
        orderRepository.save(order);
    }
}