package com.flowerShop1.repository;

import com.flowerShop1.entity.Shipper;
import com.flowerShop1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Integer> {

    // 🔍 Tìm shipper theo user_id
    Optional<Shipper> findByUser_UserId(Integer userId);

    // 🔍 Tìm shipper theo trạng thái (Available, Delivering, Inactive)
    List<Shipper> findByStatus(String status);

    // 🔍 Tìm shipper theo biển số xe
    Optional<Shipper> findByVehicleNumber(String vehicleNumber);

    // ✅ Lấy tất cả shipper theo user (phòng khi 1 user có thể có nhiều record shipper)
    List<Shipper> findAllByUser(User user);
}
