package com.flowerShop1.service.address.impl;

import com.flowerShop1.entity.Address;
import com.flowerShop1.repository.AddressRepository;
import com.flowerShop1.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceimpl  implements AddressService {
@Autowired
private AddressRepository addressRepository;
    @Override
    public void save(Address address) {
        addressRepository.save(address);

    }
}
