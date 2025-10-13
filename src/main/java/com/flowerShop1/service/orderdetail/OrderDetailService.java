package com.flowerShop1.service.orderdetail;

import com.flowerShop1.dto.product.ProductTopSellingDTO;
import com.flowerShop1.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<ProductTopSellingDTO> getTopSelilingProducts();
    List<ProductTopSellingDTO> getTrendingProducts();

}
