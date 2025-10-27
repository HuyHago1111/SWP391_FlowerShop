package com.flowerShop1.dto.address;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDTO {
    private int addressId;
    private int userId;
    private String fullName;
    private String phone;
    private String district;
    private String addressDetail;
}
