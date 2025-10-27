package com.flowerShop1.controller.client;

import com.flowerShop1.dto.user.UserLoginDTO;
import jakarta.servlet.http.HttpServletRequest;
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
                                HttpServletRequest request,
                                Model model) {

        UserLoginDTO dto = new UserLoginDTO();
        model.addAttribute("userLoginDTO", dto);

        if (error != null) {
            // ✅ Lấy thông báo lỗi cụ thể từ session
            String loginError = (String) request.getSession().getAttribute("loginError");
            model.addAttribute("loginError", loginError != null ? loginError : "Sai email hoặc mật khẩu!");
            request.getSession().removeAttribute("loginError"); // tránh lặp lại lỗi
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Bạn đã đăng xuất thành công!");
        }

        return "client/login";
    }
}
