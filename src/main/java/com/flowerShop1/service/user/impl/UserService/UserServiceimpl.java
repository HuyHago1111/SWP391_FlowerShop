package com.flowerShop1.service.user.impl.UserService;

import com.flowerShop1.entity.User;
import com.flowerShop1.repository.UserRepository;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
