package com.flowerShop1.controller.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowerShop1.dto.product.CartItermDTO;
import com.flowerShop1.entity.Product;
import com.flowerShop1.mapper.product.CartDTOMapper;
import com.flowerShop1.service.cart.CartService;
import com.flowerShop1.service.product.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/addToCart")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> responseData = new HashMap<>();


        try {
            Product product = productService.getProductById(productId);
            if (product.getStockQuantity() < quantity) {
                responseData.put("message", "Số lượng trong kho không đủ");
                responseData.put("status", "error");
                return responseData;


            }
            cartService.addToCart(CartDTOMapper.entityToDTO(product, quantity), request, response);
        } catch (RuntimeException e) {
            responseData.put("message", "Lỗi khi thêm vào giỏ hàng");
            responseData.put("status", "error");
            return responseData;
        }
        responseData.put("message", "Thêm vào giỏ hàng thành công");
        responseData.put("status", "success");
        return responseData;
    }


    @GetMapping("/showCart")

    public String getCart(HttpServletRequest request, Model model) {
        model.addAttribute("lsCart", cartService.getlsCart(request));
        model.addAttribute("sumTotalCart",(long)(cartService.getlsCart(request).stream().mapToDouble(CartItermDTO::getTotalPrice).sum()));

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

    public String updateQuantity(Model model, @RequestParam("productId") int productId, @RequestParam("quantity") int quantity, HttpServletRequest request, HttpServletResponse response) {
        cartService.updateQuantity(productId, quantity, request, response);
        model.addAttribute("lsCart", cartService.getlsCart(request));


        return "client/component/cartTable";
    }
    @GetMapping("/removeItem")
    public String removeIterm(Model model, @RequestParam("productId") int productId, HttpServletRequest request, HttpServletResponse response) {
        cartService.removeCartItem(productId, request, response);
        model.addAttribute("lsCart", cartService.getlsCart(request));
        return "client/component/cartTable";
    }
    @GetMapping("/checkout")
    public String checkout( Model model, HttpServletRequest request,HttpServletResponse response) {
        model.addAttribute("lsCart", cartService.getlsCart(request));
        model.addAttribute("sumTotalCart",(long)(cartService.getlsCart(request).stream().mapToDouble(CartItermDTO::getTotalPrice).sum()));
        return "client/checkout";
    }
}
