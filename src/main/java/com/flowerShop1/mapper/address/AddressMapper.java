package com.flowerShop1.mapper.address;

import com.flowerShop1.dto.address.AddressDTO;
import com.flowerShop1.entity.Address;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    @Autowired
    private UserService userService;
    public Address DTOtoEntity(AddressDTO addressDTO){
        Address address = new Address();
        address.setAddressId(addressDTO.getAddressId());
        address.setUser(userService.getUserById(addressDTO.getUserId()));
        address.setFullName(addressDTO.getFullName());
        address.setPhone(addressDTO.getPhone());
        address.setAddressDetail(addressDTO.getAddressDetail());
        address.setDistrict(addressDTO.getDistrict());
        return address;
    }
    public AddressDTO entityToDTO(Address address){
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressId(address.getAddressId());
        addressDTO.setUserId(address.getUser().getUserId());
        addressDTO.setFullName(address.getFullName());
        addressDTO.setPhone(address.getPhone());
        addressDTO.setAddressDetail(address.getAddressDetail());
        addressDTO.setDistrict(address.getDistrict());
        addressDTO.setDefault(address.isDefault());
        return addressDTO;
    }
}
