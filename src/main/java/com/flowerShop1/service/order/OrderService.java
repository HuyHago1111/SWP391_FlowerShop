package com.flowerShop1.service.order;

import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.OrderDetail;
import com.flowerShop1.entity.OrderStatus;
import com.flowerShop1.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    Page<Order> getOrdersByUserId(int userId, org.springframework.data.domain.Pageable pageable);
    Order save(Order order);

    Page<Order> searchOrders(String keyword, String paymentMethod, Integer statusId,
                             String sortBy, String sortDir, Pageable pageable,
                             LocalDateTime fromDate, LocalDateTime toDate,
                             Double minTotal, Double maxTotal);

    Optional<Order> getById(Integer orderId);
    Optional<OrderStatus> getStatusById(Integer statusId);
    List<OrderStatus> getAllStatuses();
    void updateOrderStatus(Integer orderId, Integer statusId); // update tracking status
    void assignShipper(Integer orderId, Integer shipperId);
    List<OrderDetail> getOrderDetails(int orderId);
    Payment getPaymentByOrderId(int orderId);
    Order getOrderWithRelations(int id);
    Optional<Order> findByIdWithAllRelations(Integer id);
//    void updateStatusByShipper(Integer orderId, Integer newStatusId, Integer shipperId, String note) throws IllegalArgumentException;
    List<Order> findOrdersForShipperWithFilter(int shipperId,
                                               String searchName,
                                               String paymentMethod,
                                               Integer statusId,
                                               String sortField,
                                               String sortDir,
                                               int page, int size);
}
