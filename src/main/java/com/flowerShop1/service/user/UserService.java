package com.flowerShop1.service.user;

import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void register(UserSignUpDTO user);
    boolean verifyOTP(String otp, UserSignUpDTO userSignUpDTO);
    void updateStatus(Integer userId, String newStatus);
}
