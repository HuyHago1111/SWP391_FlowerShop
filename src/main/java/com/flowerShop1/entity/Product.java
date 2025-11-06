package com.flowerShop1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    @Column(name = "product_name", nullable = false, length = 150, columnDefinition = "nvarchar(150)")
    private String productName;
    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "Price must be greater than 0 VND")
    @DecimalMax(value = "10000000.00", message = "Price cannot exceed 10,000,000 VND")
    private BigDecimal price;
    @Column(name = "stock_quantity", columnDefinition = "int default 0")
    @Min(value = 0, message = "Stock cannot be negative")
    @Max(value = 300, message = "Stock cannot exceed 300")
    private Integer stockQuantity;
    @Column(name = "image_url", length = 255, columnDefinition = "nvarchar(255)")
    private String imageUrl;
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "nvarchar(20)")
    private String status;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
