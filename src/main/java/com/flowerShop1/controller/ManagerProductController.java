package com.flowerShop1.controller;

import com.flowerShop1.entity.Product;
import com.flowerShop1.repository.CategoryRepository;
import com.flowerShop1.repository.SuppliersRepository;
import com.flowerShop1.service.product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/manager/products")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN','STAFF')")
public class ManagerProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private SuppliersRepository suppliersRepository;

    // ---------------- LIST PRODUCTS ----------------
    @GetMapping("")
    public String listProducts(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false) Integer supplierId,
                               @RequestParam(defaultValue = "createdAt") String sortBy,
                               @RequestParam(defaultValue = "desc") String sortDir,
                               Model model) {
        int pageSize = 10;
        Page<Product> productPage = productService.searchProducts(
                keyword, categoryId, supplierId, sortBy, sortDir, PageRequest.of(page - 1, pageSize));

        if (page > productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            page = 1;
            productPage = productService.searchProducts(
                    keyword, categoryId, supplierId, sortBy, sortDir, PageRequest.of(0, pageSize));
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

    // ---------------- VIEW DETAIL ----------------
    @GetMapping("/{productId}")
    public String viewProduct(@PathVariable Integer productId, Model model) {
        Product product = productService.getById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("product", product);
        return "manager/products/view";
    }

    // ---------------- TOGGLE STATUS ----------------
    @PostMapping("/{productId}/toggle-status")
    public String toggleStatus(@PathVariable Integer productId, RedirectAttributes ra) {
        Product product = productService.getById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if ("Out of Stock".equalsIgnoreCase(product.getStatus())) {
            ra.addFlashAttribute("error", "❌ Cannot change status manually when product is Out of Stock.");
        } else {
            product.setStatus(
                    "Active".equalsIgnoreCase(product.getStatus()) ? "Inactive" : "Active");
            try {
                productService.save(product, null);
                ra.addFlashAttribute("success", "✅ Product status updated successfully!");
            } catch (IOException e) {
                ra.addFlashAttribute("error", "Error updating product status!");
            }
        }
        return "redirect:/manager/products/" + productId;
    }

    // ---------------- EDIT FORM ----------------
    @GetMapping("/{productId}/edit")
    public String editForm(@PathVariable Integer productId, Model model) {
        Product product = productService.getById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (product.getStatus() == null || product.getStatus().isEmpty()) {
            product.setStatus("Active");
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", suppliersRepository.findAll());
        return "manager/products/edit";
    }

    // ---------------- UPDATE PRODUCT ----------------
    @PostMapping("/{productId}/edit")
    public String updateProduct(@PathVariable Integer productId,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                @RequestParam(value = "supplierId", required = false) Integer supplierId,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                Model model,
                                RedirectAttributes ra) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("suppliers", suppliersRepository.findAll());
            return "manager/products/edit";
        }

        Product existing = productService.getById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductId(existing.getProductId());

        if (categoryId != null) categoryRepository.findById(categoryId).ifPresent(product::setCategory);
        else product.setCategory(existing.getCategory());

        if (supplierId != null) suppliersRepository.findById(supplierId).ifPresent(product::setSupplier);
        else product.setSupplier(existing.getSupplier());

        if (product.getStatus() == null) product.setStatus(existing.getStatus());
        if (product.getImageUrl() == null || product.getImageUrl().isEmpty())
            product.setImageUrl(existing.getImageUrl());
        if (product.getStockQuantity() == null)
            product.setStockQuantity(existing.getStockQuantity());
        if (product.getCreatedAt() == null)
            product.setCreatedAt(existing.getCreatedAt());

        product.setUpdatedAt(LocalDateTime.now());

        productService.save(product, imageFile);
        ra.addFlashAttribute("success", "✅ Product updated successfully!");
        return "redirect:/manager/products";
    }

    // ---------------- NEW PRODUCT FORM ----------------
    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", suppliersRepository.findAll());
        return "manager/products/new";
    }

    // ---------------- CREATE NEW PRODUCT ----------------
    @PostMapping("/new")
    public String createNewProduct(@Valid @ModelAttribute("product") Product product,
                                   BindingResult result,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   @RequestParam("categoryId") Integer categoryId,
                                   @RequestParam("supplierId") Integer supplierId,
                                   Model model,
                                   RedirectAttributes ra) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("suppliers", suppliersRepository.findAll());
            return "manager/products/new";
        }

        categoryRepository.findById(categoryId).ifPresent(product::setCategory);
        suppliersRepository.findById(supplierId).ifPresent(product::setSupplier);

        product.setStatus("Active"); // auto Active when add new
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productService.save(product, imageFile);
        ra.addFlashAttribute("success", "✅ Product added successfully!");
        return "redirect:/manager/products";
    }

    // ---------------- DELETE PRODUCT ----------------
    @DeleteMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable Integer productId, RedirectAttributes ra) {
        productService.deleteById(productId);
        ra.addFlashAttribute("success", "🗑 Product deleted successfully!");
        return "redirect:/manager/products";
    }
}
