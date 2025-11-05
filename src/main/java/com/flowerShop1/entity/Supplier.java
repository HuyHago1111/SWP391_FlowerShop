package com.flowerShop1.entity;
// supplier_id  int identity
//        primary key,
//    user_id      int           not null
//        references Users,
//    company_name nvarchar(100) not null,
//    contact_name nvarchar(100),
//    phone        nvarchar(20),
//    address      nvarchar(255),
//    status       nvarchar(20) default 'Pending'
//        check ([status] = 'Rejected' OR [status] = 'Approved' OR [status] = 'Pending'),
//    created_at   datetime     default getdate()

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Supplier {
    @Id
    @Column (name = "supplier_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer supplierId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "company_name", nullable = false, length = 100, columnDefinition = "nvarchar(100)")
    private String companyName;
    @Column(name = "contact_name", length = 100, columnDefinition = "nvarchar(100)")
    private String contactName;
    @Column(name = "phone", length = 20, columnDefinition = "nvarchar(20)" )
    private String phone;
    @Column(name = "address", length = 255, columnDefinition = "nvarchar(255)" )
    private String address;
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "nvarchar(20)" )
    private String status;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
