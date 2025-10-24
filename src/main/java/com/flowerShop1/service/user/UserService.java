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
//    List<User> getAllUsers();

    Page<User> searchUsers(String keyword, String status, Pageable pageable);

//    Page<User> getAllUsers(Pageable pageable);
    void register(UserSignUpDTO user);
    boolean verifyOTP(String otp, UserSignUpDTO userSignUpDTO);
    Optional<User> findUserById(Integer id);
    void createUser(UserCreationDTO userDTO);
    List<Order> findOrdersByUserId(Integer userId);
    void updateUserStatus(Integer userId, String newStatus);
    void deleteUserById(Integer userId);
}
