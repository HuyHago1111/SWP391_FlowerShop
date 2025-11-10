// src/main/java/com/flowerShop1/service/user/UserService.java

package com.flowerShop1.service.user;

import com.flowerShop1.dto.user.UserCreationDTO;
import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
@Service

public interface UserService {
    List<User> getAllUsers();
    void register(UserSignUpDTO user);
    boolean verifyOTP(String otp, UserSignUpDTO userSignUpDTO);
    Optional<User> findUserById(Integer id);
    User createUser(UserCreationDTO userDTO);
    List<Order> findOrdersByUserId(Integer userId);
    User getUserById(int userId);

    Page<User> searchByKeywordAndRole(String keyword, String role, Pageable pageable);
    void updateStatus(Integer userId, String newStatus);

    User getUserByEmail(String email);
    void save (User user);
    void forgotPassword(UserSignUpDTO userSignUpDTO);
//    User findByEmail(String email);


    Page<User> searchUsers(String keyword, String status, Pageable pageable);
    void updateUserRole(Integer userId, Integer newRoleId);
    void updateUserStatus(Integer userId, String newStatus);
    void deleteUserById(Integer userId);
     // ✅ THÊM PHƯƠNG THỨC MỚI
    void resendOtp(UserSignUpDTO userSignUpDTO);
}
