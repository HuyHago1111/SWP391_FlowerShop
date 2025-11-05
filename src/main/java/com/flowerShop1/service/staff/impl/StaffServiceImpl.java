package com.flowerShop1.service.staff.impl;

import com.flowerShop1.entity.User;
import com.flowerShop1.repository.UserRepository;
import com.flowerShop1.service.staff.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> getAllStaff(Pageable pageable) {
        return userRepository.findByRole_RoleNameIgnoreCase("Staff", pageable);
    }

    @Override
    public Page<User> searchStaff(String keyword, Pageable pageable) {
        return userRepository.findByRole_RoleNameIgnoreCaseAndFullNameContainingIgnoreCase("Staff", keyword, pageable);
    }


    @Override
    public Optional<User> getStaffById(Integer userId) {
        return userRepository.findById(userId)
                .filter(u -> u.getRole().getRoleName().equalsIgnoreCase("Staff"));
    }

    @Override
    public void updateStatus(Integer userId, String newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        user.setStatus(newStatus);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void deleteStaffById(Integer userId) {
        userRepository.deleteById(userId);
    }
}
