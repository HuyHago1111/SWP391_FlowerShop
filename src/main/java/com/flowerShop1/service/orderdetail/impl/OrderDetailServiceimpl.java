package com.flowerShop1.service.orderdetail.impl;

import com.flowerShop1.dto.product.ProductTopSellingDTO;
import com.flowerShop1.entity.OrderDetail;
import com.flowerShop1.repository.OrderDetailRepository;
import com.flowerShop1.service.orderdetail.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class OrderDetailServiceimpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<ProductTopSellingDTO> getTopSelilingProducts() {
        List<Object[]> results = orderDetailRepository.findBestSellingProductsNative();
        return  results.stream().map(objects -> new ProductTopSellingDTO((int) objects[0], (String) objects[1], (String) objects[2], (BigDecimal) objects[3])).toList();


    }

    @Override
    public List<ProductTopSellingDTO> getTrendingProducts() {
        List<Object[]> results = orderDetailRepository.findTrendingProductsNative();
        return  results.stream().map(objects -> new ProductTopSellingDTO((int) objects[0], (String) objects[1], (String) objects[2], (BigDecimal) objects[3])).toList();
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }


}
