package com.flowerShop1.controller.client;


import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.mapper.user.UserSignUpMapper;
import com.flowerShop1.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
           userService.register(userSignUpMapper.dtoToEntity(userSignUpDTO));
           return "redirect:/login";
       } catch (Exception e) {
           model.addAttribute("error", e.getMessage());

       }
        return  "client/sign-up";
    }



}
