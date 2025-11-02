// src/main/java/com/flowerShop1/controller/client/ForgotController.java

package com.flowerShop1.controller.client;

import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.entity.User;
import com.flowerShop1.mapper.user.UserSignUpMapper;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Thêm import này

@Controller
public class ForgotController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSignUpMapper userSignUpMapper;

    @GetMapping("/forgot")
    public String forgotPassword() {
        return "client/forgot";
    }

    @PostMapping("/forgot")
    public String handleForgotForm(@RequestParam("email") String email, Model model) {
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
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @RequestParam("email") String email,
                                 Model model,
                                 RedirectAttributes redirectAttributes) { // Thêm RedirectAttributes
        if (!password.equals(confirmPassword)){
            model.addAttribute("errorMessage", "Password not match");
            model.addAttribute("email", email);
            return "client/changePassword";
        }
        User user = userService.getUserByEmail(email);
        if (user != null) {
            user.setPassword(password); // Bạn nên mã hóa mật khẩu ở đây
            userService.save(user);
            // Thêm flash attribute để hiển thị thông báo sau khi redirect
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
            return "redirect:/login"; // SỬA Ở ĐÂY: Chuyển hướng về trang login
        } else {
            model.addAttribute("errorMessage", "User not found.");
            return "client/changePassword";
        }
    }
}