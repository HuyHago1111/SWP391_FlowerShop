package com.flowerShop1.controller.client;

import com.flowerShop1.dto.user.UserEditProfileDTO;
import com.flowerShop1.entity.User;
import com.flowerShop1.mapper.user.UserEditProfileMapper;
import com.flowerShop1.service.order.OrderService;
import com.flowerShop1.service.product.ProductService;
import com.flowerShop1.service.sercurity.CustomUserDetailService;
import com.flowerShop1.service.sercurity.CustomUserDetails;
import com.flowerShop1.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserDashBoardController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserEditProfileMapper userEditProfileMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @GetMapping("/user/dashboard")
    public String userDashboard(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (customUserDetails == null) {
            return "redirect:/login";
        }
        System.out.println("[DEBUG] CustomUserDetails: " + customUserDetails.getUserId());
        User user = userService.getUserById(customUserDetails.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("userEditProfileDTO", userEditProfileMapper.entityToDTO(user));

        model.addAttribute("totalOrder", orderService.getOrdersByUserId(user.getUserId()).size());
        model.addAttribute("totalOrderPending", orderService.getOrdersByUserId(user.getUserId()).stream().filter(order -> order.getOrderStatus().getStatusId() == 1).toList().size());
        return "client/user-dashboard";
    }

    @PostMapping("/user/edit-profile")
    @ResponseBody
    public Map<String, Object> EditProfile(@Valid @RequestBody UserEditProfileDTO userEditProfileDTO, BindingResult result, Model model) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("[DEBUG] UserEditProfileDTO: " + userEditProfileDTO);
        if (result.hasErrors()) {

            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            response.put("status", "error");
            response.put("errors", errors);
            response.put("data", userEditProfileDTO);
            return response;
        }
        User user = userEditProfileMapper.dtoToEntity(userEditProfileDTO);
        userService.save(user);
        response.put("status", "success");
        response.put("message", "Profile updated successfully");
        response.put("data", userEditProfileDTO);
        return response;
    }
@GetMapping("/user/products-of-order")
    @ResponseBody
    public Map<String, Object> getProductsOfOrderByUserId(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(defaultValue = "0",name = "page") int page, @RequestParam(defaultValue = "10",name = "size") int size) {

        Map<String, Object> response = new HashMap<>();
        Pageable pageable = (Pageable) PageRequest.of(page, size);

            response.put("status", "error");
            response.put("data", productService.getProductsOfOrderByUserId(customUserDetails.getUserId(), pageable));
            return response;
        }


}

