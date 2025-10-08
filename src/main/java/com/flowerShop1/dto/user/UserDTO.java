package com.flowerShop1.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    //User ID	Full Name	Email	Phone	Address	Role	Status	Created At	Updated At	Action
    private int userId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String roleName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
