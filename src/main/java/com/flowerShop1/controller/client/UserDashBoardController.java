package com.flowerShop1.controller.client;

import com.flowerShop1.dto.user.UserEditProfileDTO;
import com.flowerShop1.entity.User;
import com.flowerShop1.mapper.user.UserEditProfileMapper;
import com.flowerShop1.service.order.OrderService;
import com.flowerShop1.service.sercurity.CustomUserDetailService;
import com.flowerShop1.service.sercurity.CustomUserDetails;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserDashBoardController {
    @Autowired
    private UserService UserService;
    @Autowired
    private UserEditProfileMapper UserEditProfileMapper;
    @Autowired
    private OrderService orderService;
    @GetMapping("/user/dashboard")
    public String userDashboard(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if(customUserDetails == null){
            return "redirect:/login";
        }
        System.out.println("[DEBUG] CustomUserDetails: " + customUserDetails.getUserId());
        User user = UserService.getUserById(customUserDetails.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("userEditProfileDTO", UserEditProfileMapper.entityToDTO(user));
        model.addAttribute("totalOrder",orderService.getOrdersByUserId(user.getUserId()).size());
        model.addAttribute("totalOrderPending",orderService.getOrdersByUserId(user.getUserId()).stream().filter(order ->order.getOrderStatus().getStatusId()==1).toList().size());
        return "client/user-dashboard";
    }
    @PostMapping("/user/edit-profile")
    @ResponseBody
    public String EditProfile(@ModelAttribute("userEditProfileDTO")UserEditProfileDTO userEditProfileDTO,Model model) {
        System.out.println("[DEBUG] UserEditProfileDTO: " + userEditProfileDTO);
        User user = UserEditProfileMapper.dtoToEntity(userEditProfileDTO);
        UserService.save(user);
        return "success";
    }
    }

