package com.flowerShop1.mapper.order;

import com.flowerShop1.dto.Order.OrderUserDashDTO;
import com.flowerShop1.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    
    public static OrderUserDashDTO entityToDTO(Order order) {
        OrderUserDashDTO orderUserDashDTO = new OrderUserDashDTO();
        orderUserDashDTO.setOrderId(order.getOrderId());
        orderUserDashDTO.setOrderStatus(order.getOrderStatus().getStatusName());
        orderUserDashDTO.setPaymentStatus(order.getPaymentStatus().getPayStatusName());
        orderUserDashDTO.setTotalAmount(order.getTotalAmount().doubleValue());
        orderUserDashDTO.setShippingAddress(order.getShippingAddress());
        orderUserDashDTO.setOrderDate(order.getOrderDate());
        orderUserDashDTO.setNote(order.getNote());
        return orderUserDashDTO;
    }
}
