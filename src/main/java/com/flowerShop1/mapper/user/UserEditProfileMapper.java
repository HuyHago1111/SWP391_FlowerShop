package com.flowerShop1.mapper.user;

import com.flowerShop1.dto.user.UserEditProfileDTO;
import com.flowerShop1.entity.User;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEditProfileMapper {
    @Autowired
    private UserService userService;


    public UserEditProfileDTO entityToDTO(User entity) {
        return new UserEditProfileDTO(
                entity.getUserId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress()
        );

    }
    public User dtoToEntity(UserEditProfileDTO dto) {
        User entity = userService.getUserById(dto.getUserId());
        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());

        return entity;
    }

}
