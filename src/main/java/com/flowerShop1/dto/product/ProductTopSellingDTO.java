package com.flowerShop1.dto.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductTopSellingDTO {
    private int prodcutId;
    private String productName;
    private String inamgeUrl;
    private BigDecimal Price;
}
