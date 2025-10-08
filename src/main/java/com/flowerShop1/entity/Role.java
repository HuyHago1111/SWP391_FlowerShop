package com.flowerShop1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

// role_id     int identity
//        primary key,
//    role_name   nvarchar(50) not null,
//    description nvarchar(255)
@Entity
@Table (name = "Roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @Column(name = "role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int roleId;
    @Column(name = "role_name", nullable = false, length = 50, columnDefinition = "nvarchar(50)")
   private String roleName;
    @Column(name = "description", length = 255, columnDefinition = "nvarchar(255)")
   private String description;
    @OneToMany (mappedBy = "role")
    private List<User> users;
}
