package com.flowerShop1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

//    payment_id     int identity
//        primary key,
//    order_id       int            not null
//        references Orders,
//    payment_method nvarchar(20)   not null
//        check ([payment_method] = 'Cash' OR [payment_method] = 'VNPay'),
//    payment_status int            not null
//        references Orders_PaymentStatus_enum,
//    transaction_id nvarchar(100),
//    amount         decimal(10, 2) not null,
//    payment_date   datetime default getdate()
@Entity
@Table(name = "Payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment {
    @Id
    @Column (name = "payment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payment_id;
    @Column(name = "order_id", nullable = false)
    private int order_id;
    @Column(name = "payment_method", nullable = false, length = 20, columnDefinition = "nvarchar(20)" )
    private String payment_method;
    @Column(name = "payment_status", nullable = false)
    private int payment_status;
    @Column(name = "transaction_id", length = 100, columnDefinition = "nvarchar(100)" )
    private String transaction_id;
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    @Column(name = "payment_date", nullable = false)
    private String payment_date;
}
