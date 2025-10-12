package com.flowerShop1.service.user.impl.UserService;

import com.flowerShop1.entity.User;
import com.flowerShop1.repository.UserRepository;
import com.flowerShop1.service.mail.MailService;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        } else {
            userRepository.save(user);
        }
        //hàm random otp 6 số ngẫu nhiên

    }
    public String generateOTP() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
}
