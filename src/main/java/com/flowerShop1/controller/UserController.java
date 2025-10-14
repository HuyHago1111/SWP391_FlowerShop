package com.flowerShop1.controller;

import com.flowerShop1.dto.user.UserCreationDTO;
import com.flowerShop1.dto.user.UserDTO;
import com.flowerShop1.entity.Order;
import com.flowerShop1.mapper.user.UserMapper;
import com.flowerShop1.repository.RoleRepository;
import com.flowerShop1.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping()
    public String getAllUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers().stream().map(userMapper::entityToDto).toList();
        model.addAttribute("users", users);
        return "indexa";
    }
    // 2. TÍNH NĂNG THÊM USER (HIỂN THỊ FORM)
    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("userDto", new UserCreationDTO());
        model.addAttribute("roles", roleRepository.findAll());
        // Trả về view: /templates/admin/add-new-user.html
        return "Admin/add-new-user";
    }

    // 2. TÍNH NĂNG THÊM USER (XỬ LÝ SUBMIT)
    @PostMapping("/save")
    public String saveNewUser(@Valid @ModelAttribute("userDto") UserCreationDTO userDto,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {

            // Nếu có lỗi validation, quay lại form và hiển thị lỗi
            if (result.hasErrors()) {
                model.addAttribute("roles", roleRepository.findAll());
                return "Admin/add-new-user";
            }

            try {
                userService.createUser( userDto);
                redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            } catch (RuntimeException e) {
                // Có thể thêm xử lý nếu email bị trùng hoặc sai Bắt lỗi từ Service
                redirectAttributes.addFlashAttribute("errorMessage" + e.getMessage());
                return "redirect:/users/add";
            }

            return "redirect:/users";
        }

        /**
         * Hiển thị lịch sử mua hàng của một người dùng cụ thể.
         */
        @GetMapping("/{userId}/orders")
        public String viewUserOrderHistory(@PathVariable("userId") Integer userId, Model model) {
            // Sử dụng Optional để xử lý trường hợp không tìm thấy user
            return userService.findUserById(userId).map(user -> {
                List<Order> orders = userService.findOrdersByUserId(userId);
                model.addAttribute("user", user);
                model.addAttribute("orders", orders);
                return "/order-detail"; // Trả về file order-detail.html
            }).orElse("error/404"); // Hoặc trang lỗi tùy chỉnh
        }
}
