package com.flowerShop1.controller;

import com.flowerShop1.entity.Supplier;
import com.flowerShop1.entity.User;
import com.flowerShop1.service.supplier.SupplierService;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/manager/suppliers")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
public class ManagerSupplierController {
    @Autowired
    private UserService userService;
    @Autowired private SupplierService supplierService;

    @GetMapping("")
    public String listSuppliers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "all") String status,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        int pageSize = 10;
        Sort sort = Sort.by(sortBy);
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

        Page<Supplier> pageSuppliers = supplierService.listSuppliers(keyword, status.equals("all") ? null : status, PageRequest.of(page - 1, pageSize, sort));

        model.addAttribute("suppliers", pageSuppliers.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageSuppliers.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "manager/suppliers/list";
    }

    // show add form
    @GetMapping("/add")
    public String addSupplierForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "manager/suppliers/add";
    }

    // handle add
    // 👉 Lưu supplier mới (với user hiện tại)
    @PostMapping("/save")
    public String addSupplier(@ModelAttribute Supplier supplier,
                              RedirectAttributes ra,
                              Principal principal) {
        try {
            // ✅ Lấy user hiện tại đang đăng nhập
            User currentUser = userService.getUserByEmail(principal.getName());
            supplier.setUser(currentUser);

            supplier.setStatus("Pending");
            supplier.setCreatedAt(LocalDateTime.now());
            supplierService.save(supplier);

            ra.addFlashAttribute("success", "✅ Supplier added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "❌ Failed to add supplier: " + e.getMessage());
        }
        return "redirect:/manager/suppliers";
    }

    // edit form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        Supplier s = supplierService.getById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
        model.addAttribute("supplier", s);
        // possible status options
        model.addAttribute("statusOptions", new String[]{"pending","approved","rejected"});
        return "manager/suppliers/edit";
    }

    // cập nhật (chỉ trạng thái hoặc thông tin liên hệ tùy chọn)
    @PostMapping("/{id}/update")
    public String updateSupplier(@PathVariable Integer id,
                                 @ModelAttribute Supplier supplier,
                                 RedirectAttributes ra) {
        try {
            Supplier existing = supplierService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));

            // cập nhật các thông tin khác nếu có
            existing.setContactName(supplier.getContactName());
            existing.setPhone(supplier.getPhone());
            existing.setAddress(supplier.getAddress());

            // kiểm tra và cập nhật trạng thái nếu thay đổi
            if (supplier.getStatus() != null &&
                    !supplier.getStatus().equalsIgnoreCase(existing.getStatus())) {
                supplierService.updateStatus(id, supplier.getStatus());
            } else {
                supplierService.save(existing);
            }

            ra.addFlashAttribute("success", "✅ Supplier updated successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "❌ Update failed: " + e.getMessage());
        }

        return "redirect:/manager/suppliers/" + id + "/edit";
    }

}
