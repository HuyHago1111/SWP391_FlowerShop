package com.flowerShop1.controller;

import com.flowerShop1.dto.user.UserDTO;
import com.flowerShop1.mapper.user.UserMapper;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping()
    public String getAllUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers().stream().map(userMapper::entityToDto).toList();
        model.addAttribute("users", users);
        return "admin/indexa";
    }
}
