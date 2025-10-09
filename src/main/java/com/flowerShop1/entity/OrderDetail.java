package com.flowerShop1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

//    order_detail_id int identity
//        primary key,
//    order_id        int            not null
//        references Orders,
//    product_id      int            not null
//        references Products,
//    quantity        int            not null,
//    price           decimal(10, 2) not null
@Entity
@Table(name = "Order_Details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetail {
    @Id
    @Column (name = "order_detail_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailId;
    @Column(name = "order_id", nullable = false)
    private int orderId;
    @Column(name = "product_id", nullable = false)
    private int productId;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

}
