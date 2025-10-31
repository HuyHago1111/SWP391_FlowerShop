package com.flowerShop1.dto.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {
    private int productId;
    private String productName;
    private double price;
    private int stockQuantity;
    private String imageUrl;


    private String categoryName;


}
