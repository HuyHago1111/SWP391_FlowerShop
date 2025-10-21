package com.flowerShop1.mapper.product;

import com.flowerShop1.dto.product.CartItermDTO;
import com.flowerShop1.entity.Product;


public   class CartDTOMapper {
    public static CartItermDTO entityToDTO(Product entity,int quantity) {
        CartItermDTO cartItermDTO = new CartItermDTO();
        cartItermDTO.setProductId(entity.getProductId());
        cartItermDTO.setProductName(entity.getProductName());
        cartItermDTO.setProductImage(entity.getImageUrl());
        cartItermDTO.setProductPrice(entity.getPrice().doubleValue());
        cartItermDTO.setStockQuantity(entity.getStockQuantity());
        cartItermDTO.setTotalPrice(entity.getPrice().doubleValue());
        cartItermDTO.setQuantityCart(quantity);
        return cartItermDTO;

    }
}
