package com.flowerShop1.dto.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserCreationDTO {

    @NotEmpty(message = "Full name is required!")
    private String fullName;

    @NotEmpty(message = "Email is required!")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Phone number is required!")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits!")
    @Pattern(regexp = "^(0|\\+84)(\\d{9})$", message = "Invalid phone number format!")
    private String phone;

    @Size(min = 8, message = "Password must be at least 8 characters long!")
    private String password;

    @NotEmpty(message = "Address is not empty!")
    private String address;

    @NotNull(message = "Role is required")
    private Integer roleId;

    private String status = "Active"; // Mặc định là Active
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}