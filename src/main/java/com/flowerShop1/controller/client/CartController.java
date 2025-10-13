package com.flowerShop1.controller.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowerShop1.dto.product.CartItermDTO;
import com.flowerShop1.entity.Product;
import com.flowerShop1.service.product.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @GetMapping("/addToCart" )
    @ResponseBody
    public String addToCart(@RequestParam ("productId") int productId, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        Product product = productService.getProductById(productId);

        return "success";
    }
@GetMapping("")
    public String getCart() {
        return "client/cart";
    }
}
