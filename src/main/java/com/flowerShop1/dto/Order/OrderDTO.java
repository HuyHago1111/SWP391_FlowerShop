package com.flowerShop1.dto.Order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {
    private int productId;
    private String productName;
    private String productImage;
    private double productPrice;
    private int StockQuantity;
    private double totalPrice;
}
