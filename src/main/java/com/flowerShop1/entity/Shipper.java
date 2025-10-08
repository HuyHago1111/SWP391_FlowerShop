package com.flowerShop1.entity;
//shipper_id     int identity
//        primary key,
//    user_id        int not null
//        references Users,
//    vehicle_number nvarchar(50),
//    phone          nvarchar(20),
//    status         nvarchar(20) default 'Available'
//        check ([status] = 'Inactive' OR [status] = 'Delivering' OR [status] = 'Available')

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Shippers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Shipper {
    @Id
    @Column(name = "shipper_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shipperId;
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Column(name = "vehicle_number", length = 50, columnDefinition = "nvarchar(50)")
    private String vehicleNumber;
    @Column(name = "phone", length = 20, columnDefinition = "nvarchar(20)" )
    private String phone;
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "nvarchar(20)" )
    private String status;
}
