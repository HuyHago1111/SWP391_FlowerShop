package com.flowerShop1.controller.client;

import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.entity.User;
import com.flowerShop1.mapper.user.UserSignUpMapper;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotController {
    @Autowired
    private final UserService userService;

    @Autowired
    private UserSignUpMapper userSignUpMapper;

    public ForgotController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/forgot")
    public String forgotPassword() {
        return "client/forgot";
    }
@PostMapping("/forgot")
    public String handleForgotFrom(@RequestParam("email") String email, Model model) {
        try{
            User user = userService.getUserByEmail(email);
            if (user == null){
                model.addAttribute("error", "User not found with this email");
                return "client/forgot";
            }
            UserSignUpDTO userSignUpDTO = userSignUpMapper.entityToDto(user);
            userService.forgotPassword(userSignUpDTO);
            model.addAttribute("userSignUpDTO", userSignUpDTO);
            model.addAttribute("flag", "forgot");

            return "client/otp";
        } catch (Exception e) {

        }
        return "client/forgot";
    }
@PostMapping("/change-password")
        public String changePassword(@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,@RequestParam("email") String email,  Model model) {
    try {
        if (!password.equals(confirmPassword)){
            model.addAttribute("errorMessage", "Password not match");
            return "client/changePassword";

        }
        User user = userService.getUserByEmail(email);
        user.setPassword(password);
        userService.save(user);
        model.addAttribute("message", "Change password successfully");
        return "client/login";
    } catch (Exception e) {
        model.addAttribute("errorMessage", e.getMessage());

    }
    return "client/changePassword";

}

}
