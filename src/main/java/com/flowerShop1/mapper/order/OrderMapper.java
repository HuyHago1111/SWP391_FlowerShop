package com.flowerShop1.mapper.order;

import com.flowerShop1.dto.Order.OrderUserDashDTO;
import com.flowerShop1.entity.Order;
import org.springframework.stereotype.Component;
import com.flowerShop1.entity.Address;

@Component
public class OrderMapper {

    public static OrderUserDashDTO entityToDTO(Order order) {
        OrderUserDashDTO orderUserDashDTO = new OrderUserDashDTO();
        orderUserDashDTO.setOrderId(order.getOrderId());
        orderUserDashDTO.setOrderStatus(order.getOrderStatus().getStatusName());
        orderUserDashDTO.setPaymentStatus(order.getPaymentStatus().getPayStatusName());
        orderUserDashDTO.setTotalAmount(order.getTotalAmount().doubleValue());
        // ✅ THAY ĐỔI LOGIC Ở ĐÂY
        orderUserDashDTO.setShippingAddress(formatAddress(order.getAddress()));
        orderUserDashDTO.setOrderDate(order.getOrderDate());
        orderUserDashDTO.setNote(order.getNote());
        return orderUserDashDTO;
    }

    // ✅ THÊM HÀM HELPER ĐỂ TẠO CHUỖI ĐỊA CHỈ
    private static String formatAddress(Address address) {
        if (address == null) {
            return "N/A"; // Hoặc một giá trị mặc định nào đó
        }
        // Xây dựng chuỗi địa chỉ từ các thành phần
        return String.format("%s, %s - SĐT: %s (%s)",
                address.getAddressDetail(),
                address.getDistrict(),
                address.getPhone(),
                address.getFullName());
    }
}
