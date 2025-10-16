package com.flowerShop1.dto.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItermDTO {
    private int productId;
    private String productName;
    private String productImage;
    private double productPrice;
    private int StockQuantity;
    private double totalPrice;
    private int quantityCart;

    public double getTotalPrice() {
        return productPrice * quantityCart;
    }
}
