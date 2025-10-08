package com.flowerShop1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

//  product_id     int identity
//        primary key,
//    category_id    int            not null
//        references Categories,
//    supplier_id    int            not null
//        references Suppliers,
//    product_name   nvarchar(150)  not null,
//    description    nvarchar(max),
//    price          decimal(10, 2) not null,
//    stock_quantity int          default 0,
//    image_url      nvarchar(255),
//    status         nvarchar(20) default 'Active'
//        check ([status] = 'Out of Stock' OR [status] = 'Inactive' OR [status] = 'Active'),
//    created_at     datetime     default getdate()
@Entity
@Table(name = "Products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Products {
    @Id
    @Column(name = "product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    @Column(name = "category_id", nullable = false)
    private int categoryId;
    @Column(name = "supplier_id", nullable = false)
    private int supplierId;
    @Column(name = "product_name", nullable = false, length = 150, columnDefinition = "nvarchar(150)")
    private String productName;
    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name = "stock_quantity", columnDefinition = "int default 0")
    private Integer stockQuantity;
    @Column(name = "image_url", length = 255, columnDefinition = "nvarchar(255)")
    private String imageUrl;
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "nvarchar(20)" )
    private String status;
    @Column(name = "created_at", nullable = false)
    private String createdAt;


}
