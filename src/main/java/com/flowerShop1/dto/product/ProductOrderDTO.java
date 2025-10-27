package com.flowerShop1.dto.product;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductOrderDTO {
    private int productId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;
    private Timestamp orderDate;
    private String orderStatus;

}
