package com.flowerShop1.service.user;

import com.flowerShop1.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void register(User user);
}
