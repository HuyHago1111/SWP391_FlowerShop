package com.flowerShop1.entity;
//  user_id       int identity
//        primary key,
//    role_id       int           not null
//        references Roles,
//    full_name     nvarchar(100) not null,
//    email         nvarchar(100) not null
//        unique,
//    password_hash nvarchar(255) not null,
//    phone         nvarchar(20),
//    address       nvarchar(255),
//    status        nvarchar(20) default 'Active'
//        check ([status] = 'Locked' OR [status] = 'Inactive' OR [status] = 'Active'),
//    created_at    datetime     default getdate(),
//    updated_at    datetime


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id" , nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int userId;
    @ManyToOne
    @JoinColumn (name = "role_id", nullable = false)

   private Role role;
    @Column(name = "full_name", nullable = false, length = 100 )
   private String fullName;
    @Column(name = "email", nullable = false, unique = true, length = 100, columnDefinition = "nvarchar(100)" )
   private String email;
    @Column(name = "password_hash", nullable = false, length = 255, columnDefinition = "nvarchar(255)")
   private String password;
    @Column(name = "phone", length = 20, columnDefinition = "nvarchar(20)")
   private String phone;
    @Column(name = "address", length = 255, columnDefinition = "nvarchar(255)")
   private String address;
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "nvarchar(20)")
   private String status;
    @Column(name = "created_at", nullable = false)
   private LocalDateTime createdAt;
    @Column(name = "updated_at")
   private LocalDateTime updatedAt;

}
