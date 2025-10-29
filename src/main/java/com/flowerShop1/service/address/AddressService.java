package com.flowerShop1.service.address;
import com.flowerShop1.entity.Address;

import java.util.List;

public  interface AddressService {
 void save (Address address);
 List<Address> getAddressesByUserId(int userId);
 boolean existsByUserIdAndAddressDetailAndDistrictAndPhoneAndFullName(Integer userId,
                                                                      String addressDetail,
                                                                      String district,
                                                                      String phone,
                                                                      String fullName);

 void deleteAddress(int addressId);
 boolean existsByUserId(int userId);
 void setDefaultAddress(int addressId);
}
