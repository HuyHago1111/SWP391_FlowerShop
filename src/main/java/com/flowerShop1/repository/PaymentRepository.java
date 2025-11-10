// File: src/main/java/com/flowerShop1/repository/PaymentRepository.java
package com.flowerShop1.repository;

import com.flowerShop1.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByOrder_OrderId(int orderId);

    // ✅ SỬA LẠI TÊN PHƯƠNG THỨC ĐỂ KHỚP VỚI ENTITY
    // Tên cũ: findTopByOrder_OrderIdOrderByPaymentDateDesc
    Payment findTopByOrder_OrderIdOrderByPaymentDateDesc(int orderId);

    List<Payment> findByOrder_OrderIdIn(List<Integer> orderIds);
}