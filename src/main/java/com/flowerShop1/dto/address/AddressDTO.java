package com.flowerShop1.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDTO {
    private int addressId;
    private int userId;
    @NotBlank( message = "Full name is required")
    private String fullName;
    @NotBlank( message = "Phone number is required")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number format")
    private String phone;
    @NotBlank( message = "District is required")
    private String district;
    @NotBlank( message = "Address detail is required")
    private String addressDetail;
    private boolean isDefault;
}
