package com.flowerShop1.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEditProfileDTO {
    private int userId;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    @Pattern(regexp = "^(0|\\+84)(\\d{9})$", message = "Invalid phone number format")
    private String phone;
    @NotBlank(message = "Address is required")
    private String address;

}
