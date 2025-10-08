package com.flowerShop1.entity;

import jakarta.persistence.*;
import lombok.*;
//  order_id         int identity
//        primary key,
//    user_id          int            not null
//        references Users,
//    shipper_id       int
//        references Shippers,
//    order_status     int            not null
//        references Orders_Status_enum,
//    payment_status   int            not null
//        references Orders_PaymentStatus_enum,
//    total_amount     decimal(10, 2) not null,
//    shipping_address nvarchar(255),
//    order_date       datetime default getdate(),
//    updated_at       datetime,
//    note             nvarchar(255)
@Entity
@Table(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Orders {
    @Id
    @Column(name = "order_id", nullable = false)
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int orderId;
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Column(name = "shipper_id")
    private Integer shipperId;
    @Column(name = "order_status", nullable = false)
    private int orderStatus;
    @Column(name = "payment_status", nullable = false)
    private int paymentStatus;
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private double totalAmount;
    @Column(name = "shipping_address", length = 255, columnDefinition = "nvarchar(255)")
    private String shippingAddress;
    @Column(name = "order_date", nullable = false)
    private String orderDate;
    @Column(name = "updated_at")
    private String updatedAt;
    @Column(name = "note", length = 255, columnDefinition = "nvarchar(255)")
    private String note;

}
