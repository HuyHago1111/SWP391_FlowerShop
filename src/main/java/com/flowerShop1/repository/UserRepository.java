package com.flowerShop1.repository;

import com.flowerShop1.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}
