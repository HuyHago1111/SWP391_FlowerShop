package com.flowerShop1.controller;
import com.flowerShop1.entity.Category;
import com.flowerShop1.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manager/categories")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
public class ManagerCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String listCategories(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(required = false) String keyword,
                                 Model model) {

        int pageSize = 10;
        Page<Category> categoryPage = categoryService.getAllCategories(keyword, PageRequest.of(page - 1, pageSize));

        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "manager/categories/listcategory";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "manager/categories/newcategory";
    }

    @PostMapping("/new")
    public String addCategory(@ModelAttribute Category category, RedirectAttributes ra) {
        categoryService.save(category);
        ra.addFlashAttribute("success", "✅ Category added successfully!");
        return "redirect:/manager/categories";
    }

    @GetMapping("/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        model.addAttribute("category", category);
        return "manager/categories/editcategory";
    }

    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Integer id, @ModelAttribute Category category, RedirectAttributes ra) {
        category.setCategoryId(id);
        categoryService.save(category);
        ra.addFlashAttribute("success", "✅ Category updated successfully!");
        return "redirect:/manager/categories";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes ra) {
        categoryService.deleteById(id);
        ra.addFlashAttribute("success", "🗑 Category deleted successfully!");
        return "redirect:/manager/categories";
    }
}

