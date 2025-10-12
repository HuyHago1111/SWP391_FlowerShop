package com.flowerShop1.controller.client;

import com.flowerShop1.dto.user.UserLoginDTO;
import jakarta.validation.Valid;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @GetMapping("")
    public String getLogin(Model model) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        model.addAttribute("userLoginDTO", userLoginDTO);
        return "client/login";
    }
    @PostMapping("")
    public String postLogin(@Valid @ModelAttribute("userLoginDTO") UserLoginDTO userLoginDTO,
                            BindingResult result,
                            Model model) {
        System.out.println("Login: " + userLoginDTO);

        if (result.hasErrors()) {
            return "client/login";
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );

            // Nếu OK -> lưu vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(auth);
            return "redirect:/";

        } catch (BadCredentialsException ex) {
            model.addAttribute("loginError", "Sai tên đăng nhập hoặc mật khẩu!");
        } catch (DisabledException ex) {
            model.addAttribute("loginError", "Tài khoản bị vô hiệu hóa!");
        } catch (LockedException ex) {
            model.addAttribute("loginError", "Tài khoản bị khóa!");
        } catch (Exception ex) {
            model.addAttribute("loginError", "Lỗi xác thực, vui lòng thử lại!");
        }


        return "client/login";
    }
}
