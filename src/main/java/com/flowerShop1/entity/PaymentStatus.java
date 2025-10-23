package com.flowerShop1.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Orders_PaymentStatus_enum")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatus {
    /*
    * create table Orders_PaymentStatus_enum
(
    pay_status_id   int identity
        primary key,
    pay_status_name nvarchar(50) not null
)
go

*/
    @Id
    @Column(name = "pay_status_id", nullable = false)
    private int payStatusId;
    @Column(name = "pay_status_name", nullable = false, length = 50, columnDefinition = "nvarchar(50)")
    private String payStatusName;
    @OneToMany(mappedBy = "paymentStatus")
    private List<Order> orders;
}
