package com.flowerShop1.service.shipper;

import com.flowerShop1.entity.Shipper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ShipperService {

    // Lấy toàn bộ shipper (danh sách)
    List<Shipper> getAllShippers();

    // Lấy toàn bộ shipper (phân trang)
    Page<Shipper> getAllShippers(Pageable pageable);

    // Tìm kiếm shipper theo tên hoặc biển số xe (keyword)
    Page<Shipper> searchShippers(String keyword, Pageable pageable);

    // Lấy shipper theo ID
    Optional<Shipper> getShipperById(Integer shipperId);

    // Lấy shipper theo UserId
    Optional<Shipper> getByUserId(Integer userId);

    // Lưu shipper mới
    Shipper saveShipper(Shipper shipper);

    // Cập nhật trạng thái (Available / Delivering / Inactive)
    void updateStatus(Integer shipperId, String newStatus);

    // Xóa shipper
    void deleteShipperById(Integer shipperId);
}
