// src/main/java/com/flowerShop1/service/order/impl/OrderServiceimpl.java
package com.flowerShop1.service.order.impl;

import com.flowerShop1.entity.*;
import com.flowerShop1.repository.*;
import com.flowerShop1.service.order.OrderService;
import com.flowerShop1.service.product.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.flowerShop1.config.OrderSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceimpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusRepository statusRepository;
    @Autowired
    private ShipperRepository shipperRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Override
    public Page<Order> getOrdersByUserId(int userId, org.springframework.data.domain.Pageable pageable) {
        return orderRepository.findByUserUserId(userId , pageable);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

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
        return orderRepository.findByIdWithRelations(orderId);
    }

    @Override
    public List<OrderDetail> getOrderDetails(int orderId) {
        return orderDetailRepository.findByOrder_OrderId(orderId);
    }

    @Override
    public Payment getPaymentByOrderId(int orderId) {
        // ✅ ĐÃ SỬA LỖI TẠI ĐÂY
        return paymentRepository.findTopByOrder_OrderIdOrderByPaymentDateDesc(orderId);
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

    @Override
    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    @Transactional
    public void cancelOrder(Integer orderId, int userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getUser().getUserId() != userId) {
            throw new RuntimeException("You are not allowed to cancel this order");
        }
        int currentStatusId = order.getOrderStatus().getStatusId();
        if (currentStatusId != 1 && currentStatusId != 2) {
            throw new RuntimeException("You are not allowed to cancel this order with status: " + order.getOrderStatus().getStatusName());
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderOrderId(orderId);
        for (OrderDetail orderDetail : orderDetails) {
            Product product = orderDetail.getProduct();
            int newStock = product.getStockQuantity() + orderDetail.getQuantity();
            product.setStockQuantity(newStock);
            productService.save(product);
        }

        OrderStatus orderStatus = statusRepository.findById(7) // Giả sử 7 là 'Cancelled'
                .orElseThrow(() -> new RuntimeException("Status 'Cancelled' not found"));
        order.setOrderStatus(orderStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public Order getOrderWithRelations(int id) {
        return orderRepository.findByOrderId(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    @Override
    public Optional<Order> findByIdWithAllRelations(Integer id) {
        return orderRepository.findByIdWithAllRelations(id);
    }

    @Override
    public List<Order> findOrdersForShipperWithFilter(int shipperId,
                                                      String searchName,
                                                      String paymentMethod,
                                                      Integer statusId,
                                                      String sortField,
                                                      String sortDir,
                                                      int page, int size) {

        List<Order> base = orderRepository.findByShipper_ShipperId(shipperId);

        if (searchName != null && !searchName.isBlank()) {
            String lower = searchName.toLowerCase(Locale.ROOT);
            base = base.stream()
                    .filter(o -> o.getUser() != null && o.getUser().getFullName() != null && o.getUser().getFullName().toLowerCase(Locale.ROOT).contains(lower))
                    .collect(Collectors.toList());
        }

        if (paymentMethod != null && !paymentMethod.isBlank()) {
            String pm = paymentMethod.trim();
            base = base.stream()
                    .filter(o -> {
                        // ✅ ĐÃ SỬA LỖI TẠI ĐÂY
                        Payment p = paymentRepository.findTopByOrder_OrderIdOrderByPaymentDateDesc(o.getOrderId());
                        if (p == null) return "COD".equalsIgnoreCase(pm);
                        return pm.equalsIgnoreCase(p.getPayment_method());
                    })
                    .collect(Collectors.toList());
        }

        if (statusId != null) {
            base = base.stream()
                    .filter(o -> o.getOrderStatus() != null && o.getOrderStatus().getStatusId() == statusId)
                    .collect(Collectors.toList());
        }

        Comparator<Order> comparator;
        if ("totalAmount".equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(Order::getTotalAmount);
        } else {
            comparator = Comparator.comparing(Order::getOrderDate);
        }

        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        base.sort(comparator);

        int from = page * size;
        int to = Math.min(from + size, base.size());
        if (from >= base.size()) {
            return Collections.emptyList();
        }
        return base.subList(from, to);
    }
}