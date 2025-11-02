package com.flowerShop1.mapper.user;

import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.entity.Role;
import com.flowerShop1.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserSignUpMapper {
    public User dtoToEntity (UserSignUpDTO dto){
        User user = new User();
        Role role = new Role();
        role.setRoleId(5);

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(role);

        return user;
    }
    public UserSignUpDTO entityToDto (User user){
        UserSignUpDTO dto = new UserSignUpDTO();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());

        return dto;
    }
}
