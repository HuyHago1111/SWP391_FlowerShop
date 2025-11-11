package com.flowerShop1.controller.client;


import com.flowerShop1.dto.product.CartItermDTO;
import com.flowerShop1.entity.Product;

import com.flowerShop1.mapper.product.CartDTOMapper;
import com.flowerShop1.service.cart.CartService;
import com.flowerShop1.service.product.ProductService;
import com.flowerShop1.service.sercurity.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // ✅ THÊM IMPORT
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @GetMapping("/addToCart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            HttpServletRequest request,
            HttpServletResponse response) {

        Map<String, Object> responseData = new HashMap<>();

        try {

            Product product = productService.getProductById(productId);
            cartService.addToCart(CartDTOMapper.entityToDTO(product, quantity), request, response);

            // THÀNH CÔNG:
            responseData.put("message", "Thêm vào giỏ hàng thành công");
            responseData.put("status", "success");
            return ResponseEntity.ok(responseData);

        } catch (IllegalArgumentException e) {

            responseData.put("message", e.getMessage());
            responseData.put("status", "error");
            return ResponseEntity.badRequest().body(responseData);

        } catch (RuntimeException e) {

            responseData.put("message", "Lỗi: " + e.getMessage());
            responseData.put("status", "error");
            return ResponseEntity.internalServerError().body(responseData);
        }
    }


    @GetMapping("/showCart")
    public String getCart(HttpServletRequest request, Model model) {
        List<CartItermDTO> lsCart = cartService.getlsCart(request);
        long sumTotalCart = (long) lsCart.stream().mapToDouble(CartItermDTO::getTotalPrice).sum();

        model.addAttribute("lsCart", lsCart);
        model.addAttribute("sumTotalCart", sumTotalCart);
        return "client/component/cartTable";
    }


    @GetMapping("")
    public String viewCart(Model model) {
        return "client/cart";
    }

    @GetMapping("/getlsCart")
    @ResponseBody
    public List<CartItermDTO> getlsCart(HttpServletRequest request) {
        return cartService.getlsCart(request);
    }


    @GetMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuantity(
            @RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            HttpServletRequest request,
            HttpServletResponse response) {

        Map<String, Object> responseData = new HashMap<>();
        try {

            cartService.updateQuantity(productId, quantity, request, response);

            responseData.put("status", "success");
            responseData.put("message", "Cập nhật thành công!");
            return ResponseEntity.ok(responseData);

        } catch (IllegalArgumentException e) {

            responseData.put("status", "error");
            responseData.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseData);

        } catch (Exception e) {
            // Bắt các lỗi khác
            responseData.put("status", "error");
            responseData.put("message", "Lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseData);
        }
    }


    @GetMapping("/removeItem")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeIterm(
            @RequestParam("productId") int productId,
            HttpServletRequest request,
            HttpServletResponse response) {

        Map<String, Object> responseData = new HashMap<>();
        try {
            cartService.removeCartItem(productId, request, response);
            responseData.put("status", "success");
            responseData.put("message", "Đã xóa sản phẩm!");
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.put("status", "error");
            responseData.put("message", "Lỗi khi xóa sản phẩm: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseData);
        }
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails == null){
            return "redirect:/login";
        }

        List<CartItermDTO> lsCart = cartService.getlsCart(request);
        long sumTotalCart = (long) lsCart.stream().mapToDouble(CartItermDTO::getTotalPrice).sum();

        model.addAttribute("lsCart", lsCart);
        model.addAttribute("sumTotalCart", sumTotalCart);
        return "client/checkout";
    }
}