package com.flowerShop1.service.staff;

import com.flowerShop1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    Page<User> getAllStaff(Pageable pageable);
    Page<User> searchStaff(String keyword, Pageable pageable);
    Optional<User> getStaffById(Integer userId);
    void updateStatus(Integer userId, String newStatus);
    void deleteStaffById(Integer userId);
}
