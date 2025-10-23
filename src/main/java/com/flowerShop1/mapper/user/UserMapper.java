package com.flowerShop1.mapper.user;

import com.flowerShop1.dto.user.UserDTO;
import com.flowerShop1.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User dtoToEntity() {
        return null;
    }
    public  UserDTO entityToDto(User dto) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(dto.getUserId());
        userDTO.setFullName(dto.getFullName());
        userDTO.setEmail(dto.getEmail());
        userDTO.setPhone(dto.getPhone());
        userDTO.setPassword(dto.getPassword());
        userDTO.setAddress(dto.getAddress());
        userDTO.setRoleName(dto.getRole().getRoleName());
        userDTO.setStatus(dto.getStatus());
        userDTO.setCreatedAt(dto.getCreatedAt());
        userDTO.setUpdatedAt(dto.getUpdatedAt());


        return userDTO;
    }
}
