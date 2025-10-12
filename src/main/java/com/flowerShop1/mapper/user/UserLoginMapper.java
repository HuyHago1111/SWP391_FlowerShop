package com.flowerShop1.mapper.user;

import com.flowerShop1.dto.user.UserLoginDTO;
import com.flowerShop1.entity.User;

public class UserLoginMapper {
    public User dtoToEntity(UserLoginDTO userLoginDTO) {
        User user = new User();
        user.setEmail(userLoginDTO.getEmail());
        user.setPassword(userLoginDTO.getPassword());
        return user;
    }

}
