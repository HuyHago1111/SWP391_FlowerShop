package com.flowerShop1.dto.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductTopSellingDTO {
    private int productId;
    private String productName;
    private String imageUrl;
    private BigDecimal price;
}
