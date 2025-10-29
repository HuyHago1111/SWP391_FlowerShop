package com.flowerShop1.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface AddressRepository  extends JpaRepository<com.flowerShop1.entity.Address, Integer> {
    boolean existsByUserUserIdAndAddressDetailAndDistrictAndPhoneAndFullName(Integer userId,
                                                                         String addressDetail,
                                                                         String district,
                                                                         String phone,
                                                                         String fullName);
    @Transactional
    void deleteById(int addressId);

    boolean existsByUserUserId(int userId);

}
