// src/main/java/com/flowerShop1/controller/client/LoginController.java

package com.flowerShop1.controller.client;

import com.flowerShop1.dto.user.UserLoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping("")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {

        // Kiểm tra xem có thông báo thành công từ redirect không
        if (model.containsAttribute("successMessage")) {
            model.addAttribute("successMessage", model.getAttribute("successMessage"));
        }

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