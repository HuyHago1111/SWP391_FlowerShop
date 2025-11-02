package com.flowerShop1.controller;

import com.flowerShop1.entity.User;
import com.flowerShop1.entity.Shipper;
import com.flowerShop1.service.user.UserService;
import com.flowerShop1.repository.ShipperRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/manager/staff-shipper")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
public class ManagerStaffAndShipperController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShipperRepository shipperRepository;

    // 📋 Trang danh sách Staff + Shipper
    @GetMapping("")
    public String listUsers(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false ,defaultValue = "all") String role,
                            Model model) {

        int pageSize = 10;

        Page<User> userPage = userService.searchByKeywordAndRole(keyword, role, PageRequest.of(page - 1, pageSize));

        // ✅ Không thay đổi kiểu dữ liệu Page<User>
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", role);

        return "manager/staff-shipper/list";
    }

    // 👁️ Trang xem chi tiết
    @GetMapping("/{userId}")
    public String viewUser(@PathVariable Integer userId, Model model) {
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("user", user);

        // Nếu là shipper → hiển thị thêm thông tin shipper
        if (user.getRole() != null && "SHIPPER".equalsIgnoreCase(user.getRole().getRoleName())) {
            Optional<Shipper> shipper = shipperRepository.findByUser_UserId(userId);
            shipper.ifPresent(s -> model.addAttribute("shipper", s));
        }

        return "manager/staff-shipper/view";
    }

    // 🔁 Toggle status (Active ↔ Pending)
    @PostMapping("/{userId}/toggle-status")
    public String toggleUserStatus(@PathVariable Integer userId, RedirectAttributes ra) {
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String newStatus = "Active".equalsIgnoreCase(user.getStatus()) ? "Pending" : "Active";
        userService.updateUserStatus(userId, newStatus);

        ra.addFlashAttribute("success", "✅ Updated user status to " + newStatus);
        return "redirect:/manager/staff-shipper/" + userId;
    }

    // 🗑️ Xóa user (nếu cần)
    @DeleteMapping("/{userId}/delete")
    public String deleteUser(@PathVariable Integer userId, RedirectAttributes ra) {
        userService.deleteUserById(userId);
        ra.addFlashAttribute("success", "🗑 User deleted successfully!");
        return "redirect:/manager/staff-shipper";
    }
}
