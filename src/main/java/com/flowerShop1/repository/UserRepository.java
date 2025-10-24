package com.flowerShop1.repository;

import com.flowerShop1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Page<User> findByFullNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<User> findByFullNameContainingIgnoreCaseAndStatusIgnoreCase(String keyword, String status, Pageable pageable);
    // 🔍 Chỉ lọc trạng thái
    Page<User> findByStatusIgnoreCase(String status, Pageable pageable);
//    Page<User> getAllUsers(Pageable pageable);
}
