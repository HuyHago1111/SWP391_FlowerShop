package com.flowerShop1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    /*
    * create table Orders
(
    order_id         int identity
        primary key,
    user_id          int            not null
        references Users,
    shipper_id       int
        references Shippers,
    order_status     int            not null
        references Orders_Status_enum,
    payment_status   int            not null
        references Orders_PaymentStatus_enum,
    total_amount     decimal(10, 2) not null,
    shipping_address nvarchar(255),
    order_date       datetime default getdate(),
    updated_at       datetime,
    note             nvarchar(255)
)
go

*/
    @Id
    @Column(name = "order_id", nullable = false)
    private int orderId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Shipper shipper;
    @ManyToOne
    @JoinColumn(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
    @ManyToOne
    @JoinColumn(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    @Column(name = "shipping_address", length = 255, columnDefinition = "nvarchar(255)")
    private String shippingAddress;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    @Column(name = "updated_at", nullable = false)
    private String updatedAt;
    @Column(name = "note", length = 255, columnDefinition = "nvarchar(255)")
    private String note;
}
