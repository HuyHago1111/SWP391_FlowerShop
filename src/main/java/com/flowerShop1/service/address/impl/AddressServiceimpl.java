package com.flowerShop1.service.address.impl;

import com.flowerShop1.entity.Address;
import com.flowerShop1.repository.AddressRepository;
import com.flowerShop1.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceimpl  implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void save(Address address) {
        addressRepository.save(address);

    }

    @Override
    public List<Address> getAddressesByUserId(int userId) {
        return addressRepository.findAll().stream().filter(address -> address.getUser().getUserId() == userId).toList();
    }

    @Override
    public boolean existsByUserIdAndAddressDetailAndDistrictAndPhoneAndFullName(Integer userId, String addressDetail, String district, String phone, String fullName) {
        return addressRepository.existsByUserUserIdAndAddressDetailAndDistrictAndPhoneAndFullName(userId, addressDetail, district, phone, fullName);
    }

    @Override
    public void deleteAddress(int addressId) {
        try {
            addressRepository.deleteById(addressId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByUserId(int userId) {
        return addressRepository.existsByUserUserId(userId);
    }

    @Override
    public void setDefaultAddress(int addressId) {
        Address address = addressRepository.findById(addressId).get();
        address.setDefault(true);
        addressRepository.findAll().stream().filter(item -> item.getUser().getUserId() == address.getUser().getUserId()).forEach(item -> {
            if (item.getAddressId() != addressId) {
                item.setDefault(false);
        addressRepository.save(item);
    }
});
        addressRepository.save(address);

    }
}
