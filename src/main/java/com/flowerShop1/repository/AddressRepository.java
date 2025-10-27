package com.flowerShop1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface AddressRepository  extends JpaRepository<com.flowerShop1.entity.Address, Integer> {

}
