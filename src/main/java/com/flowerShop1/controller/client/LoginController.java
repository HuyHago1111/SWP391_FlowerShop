package com.flowerShop1.controller.client;

import com.flowerShop1.dto.user.UserLoginDTO;
import jakarta.validation.Valid;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping("")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {

        UserLoginDTO dto = new UserLoginDTO();
        model.addAttribute("userLoginDTO", dto);

        if (error != null) {
            model.addAttribute("loginError", "Sai email hoặc mật khẩu!");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Bạn đã đăng xuất thành công!");
        }

        return "client/login";
    }
}
