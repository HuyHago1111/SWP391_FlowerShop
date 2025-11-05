package com.flowerShop1.dto.Order;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderUserDashDTO {
    private int orderId;
    private String orderStatus;
    private String paymentStatus;
    private double totalAmount;
    private String shippingAddress;
    private LocalDateTime orderDate;
    private String note;
    
}
