package com.flowerShop1.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserCreationDTO {

    @NotEmpty(message = "Full name is required")
    private String fullName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Phone number is required")

    private String phone;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String address;

    @NotNull(message = "Role is required")
    private Integer roleId;

    private String status = "Active"; // Mặc định là Active
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}