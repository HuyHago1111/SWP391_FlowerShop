package com.flowerShop1.controller;
import org.springframework.data.domain.Pageable;
import com.flowerShop1.dto.user.UserCreationDTO;
import com.flowerShop1.dto.user.UserDTO;
import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.Role;
import com.flowerShop1.entity.User;
import com.flowerShop1.mapper.user.UserMapper;
import com.flowerShop1.repository.RoleRepository;
import com.flowerShop1.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;

//    @GetMapping("/all")
//    public String getAllUsers(Model model) {
//        List<UserDTO> users = userService.getAllUsers().stream().map(userMapper::entityToDto).toList();
//        model.addAttribute("users", users);
//        return "Admin/indexa";
//    }
    @GetMapping("")
    public String listUsers(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false, defaultValue = "all") String status,
                            Model model) {

        int pageSize = 10;
        Page<User> userPage = userService.searchUsers(keyword, status, PageRequest.of(page - 1, pageSize));

        // Nếu page hiện tại vượt quá totalPages → quay về trang 1
        if (page > userPage.getTotalPages() && userPage.getTotalPages() > 0) {
            page = 1;
            userPage = userService.searchUsers(keyword, status, PageRequest.of(0, pageSize));
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "Admin/indexa";
    }


    // 2. TÍNH NĂNG THÊM USER (HIỂN THỊ FORM)
    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("userDto", new UserCreationDTO());
        List<Role> allRoles = roleRepository.findAll(); // load all roles from DB/service
        Set<String> allowed = Set.of("customer", "manager", "staff", "shipper", "supplier");
        List<Role> filtered = allRoles.stream()
                .filter(r -> r.getRoleName() != null && allowed.contains(r.getRoleName().toLowerCase()))
                .collect(Collectors.toList());
        model.addAttribute("roles", filtered);
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

    @GetMapping("/update-role/{userId}")
    public String showUpdateRoleForm(@PathVariable("userId") Integer userId, Model model ,RedirectAttributes redirectAttributes) {
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        return "Admin/update-role";
    }

    @PostMapping("/update-role/{userId}")
    public String updateUserRole(@PathVariable("userId") Integer userId,
                                 @RequestParam("newRoleId") Integer newRoleId,
                                 RedirectAttributes redirectAttributes) {

        try {
            userService.updateUserRole(userId, newRoleId);
            redirectAttributes.addFlashAttribute("successMessage", "✅ Cập nhật quyền người dùng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Lỗi khi cập nhật quyền: " + e.getMessage());
        }

        return "redirect:/users";
    }

    /**
         * Hiển thị lịch sử mua hàng của một người dùng cụ thể.
         */
        @GetMapping("/lo")
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
