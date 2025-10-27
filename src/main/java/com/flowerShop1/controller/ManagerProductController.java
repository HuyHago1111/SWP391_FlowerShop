package com.flowerShop1.controller;

import com.flowerShop1.entity.Product;
import com.flowerShop1.repository.CategoryRepository;
import com.flowerShop1.repository.SuppliersRepository;

import com.flowerShop1.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Controller
@RequestMapping("/manager/products")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN' ,'STAFF')")
public class ManagerProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private SuppliersRepository suppliersRepository;

    @GetMapping("")
    public String listProducts(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false) Integer supplierId,
                               @RequestParam(defaultValue = "createdAt") String sortBy,
                               @RequestParam(defaultValue = "desc") String sortDir,
                               Model model) {
        int pageSize = 10;
        Page<Product> productPage = productService.searchProducts(keyword, categoryId, supplierId, sortBy, sortDir, PageRequest.of(page - 1, pageSize));

        // Nếu page hiện tại vượt quá totalPages → quay về trang 1
        if (page > productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            page = 1;
            productPage = productService.searchProducts(keyword, categoryId, supplierId, sortBy, sortDir, PageRequest.of( 0, pageSize));
        }
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("supplierId", supplierId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", suppliersRepository.findAll());
        return "manager/products/list";
    }

    @GetMapping("/{productId}")
    public String viewProduct(@PathVariable Integer productId, Model model) {
        Product product = productService.getById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("product", product);
        return "manager/products/view";
    }

    @PostMapping("/{productId}/toggle-status")
    public String toggleStatus(@PathVariable Integer productId, RedirectAttributes ra) {
        Product product = productService.getById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if ("Out of Stock".equalsIgnoreCase(product.getStatus())) {
            ra.addFlashAttribute("error", "❌ Cannot change status manually when product is Out of Stock.");
        } else {
            if ("Active".equalsIgnoreCase(product.getStatus())) product.setStatus("Inactive");
            else product.setStatus("Active");
            try {
                productService.save(product, null);
                ra.addFlashAttribute("success", "✅ Product status updated successfully!");
            } catch (IOException e) {
                ra.addFlashAttribute("error", "Error updating product status!");
            }
        }
        return "redirect:/manager/products/" + productId;
    }

    @GetMapping("/{productId}/edit")
    public String editForm(@PathVariable Integer productId, Model model) {
        Product product = productService.getById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", suppliersRepository.findAll());
        return "manager/products/edit";
    }

    @PostMapping("/{productId}/edit")
    public String updateProduct(@PathVariable Integer productId,
                                @ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                RedirectAttributes ra) throws IOException {
        product.setProductId(productId);
        productService.save(product, imageFile);
        ra.addFlashAttribute("success", "✅ Product updated successfully!");
        return "redirect:/manager/products";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", suppliersRepository.findAll());
        return "manager/products/new";
    }

    @PostMapping("/new")
    public String createNewProduct(@ModelAttribute Product product,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   RedirectAttributes ra) throws IOException {
        productService.save(product, imageFile);
        ra.addFlashAttribute("success", "✅ Product added successfully!");
        return "redirect:/manager/products";
    }

    @DeleteMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable Integer productId, RedirectAttributes ra) {
        productService.deleteById(productId);
        ra.addFlashAttribute("success", "🗑 Product deleted successfully!");
        return "redirect:/manager/products";
    }
}