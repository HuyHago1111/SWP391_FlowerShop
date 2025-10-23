package com.flowerShop1.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Orders_Status_enum")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus {
    /*
    * create table Orders_Status_enum
(
    status_id   int identity
        primary key,
    status_name nvarchar(50) not null
)
go

*/
    @Id
    @Column(name = "status_id", nullable = false)
    private int statusId;
    @Column(name = "status_name", nullable = false, length = 50, columnDefinition = "nvarchar(50)")
    private String statusName;
    @OneToMany(mappedBy = "orderStatus")
    private List<Order> orders;

}
