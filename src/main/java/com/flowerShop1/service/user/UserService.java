package com.flowerShop1.service.user;

import com.flowerShop1.dto.user.UserCreationDTO;
import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.User;

import org.springframework.stereotype.Service;
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
    void save (User user);
    void updateStatus(Integer userId, String newStatus);
}
