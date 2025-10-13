package com.flowerShop1.controller.client;


import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.mapper.user.UserSignUpMapper;
import com.flowerShop1.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sign-up")

public class SignUpController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserSignUpMapper userSignUpMapper;

    @GetMapping("")
    public String signUp(Model model) {
        UserSignUpDTO userSignUpDTO = new UserSignUpDTO();
        model.addAttribute("userSignUpDTO", userSignUpDTO);

        return "client/sign-up";
    }
    @PostMapping("")
    public String register(@Valid @ModelAttribute("userSignUpDTO") UserSignUpDTO userSignUpDTO, BindingResult bindingResult, Model model) {
        System.out.println("[DEBUG] User sign up:" + userSignUpDTO);
        if (bindingResult.hasErrors()) {
            return  "client/sign-up";

        }

       try{

           userService.register(userSignUpDTO);
           model.addAttribute("userSignUpDTO", userSignUpDTO);
           return "client/otp";
       } catch (Exception e) {
           model.addAttribute("error", e.getMessage());

       }
        return  "client/sign-up";
    }

@PostMapping ("/verify-otp")
    public String verifyOtp(@ModelAttribute("userSignUpDTO") UserSignUpDTO userSignUpDTO, Model model, @RequestParam("otpInput") String otp) {
    System.out.println("[DEBUG] VerifyOTP : " + otp);

    if(userService.verifyOTP(otp, userSignUpDTO)){
        model.addAttribute("userLoginDTO", userSignUpDTO);
        return "client/login";
    }

        return "client/sign-up";
    }

}
