package com.flowerShop1.repository;



import com.flowerShop1.entity.PaymentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {
    // ✅ Thêm method tìm payment status theo tên
    PaymentStatus findByPayStatusName(String payStatusName);
}
