// src/main/java/com/flowerShop1/dto/Order/OrderDTO.java
package com.flowerShop1.dto.Order;

import com.flowerShop1.entity.Order;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {
    // Các trường dữ liệu mới phù hợp với thông tin đơn hàng
    private int orderId;
    private String customerName;
    private LocalDateTime orderDate;
    private String paymentMethod;
    private String orderStatus;
    private BigDecimal totalAmount;
    private String statusCssClass; // Thêm trường này để xử lý màu sắc trên giao diện

    // Constructor để dễ dàng chuyển đổi từ Entity sang DTO
    public OrderDTO(Order order) {
        this.orderId = order.getOrderId();
        this.customerName = order.getUser().getFullName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus().getStatusName();
        this.totalAmount = order.getTotalAmount();
        this.statusCssClass = getStatusCssClass(order.getOrderStatus().getStatusName());
    }

    // Helper method để xác định class CSS cho trạng thái
    private String getStatusCssClass(String statusName) {
        if (statusName == null) return "status-inactive";
        return switch (statusName.toLowerCase()) {
            case "đã hoàn thành", "approved" -> "status-active";
            case "đã hủy", "thất bại", "rejected" -> "status-outofstock";
            case "chờ xử lý", "pending" -> "status-inactive";
            default -> "status-pending"; // Dành cho các trạng thái còn lại như Đang giao, Đang chuẩn bị...
        };
    }
}