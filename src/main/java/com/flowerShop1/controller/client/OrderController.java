package com.flowerShop1.controller.client;

import com.flowerShop1.dto.product.CartItermDTO;
import com.flowerShop1.entity.*;
import com.flowerShop1.repository.AddressRepository;
import com.flowerShop1.service.cart.CartService;
import com.flowerShop1.service.order.OrderService;
import com.flowerShop1.service.orderdetail.OrderDetailService;
import com.flowerShop1.service.product.ProductService;
import com.flowerShop1.service.sercurity.CustomUserDetails;
import com.flowerShop1.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Thêm import này

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AddressRepository addressRepository;

    // ✅ THAY ĐỔI TOÀN BỘ PHƯƠNG THỨC NÀY
    @PostMapping("/order/create-cod")
    public String createCodOrder(
            @RequestParam("addressId") int addressId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) { // Sử dụng RedirectAttributes

        try {
            List<CartItermDTO> lsCart = cartService.getlsCart(request);
            if (lsCart == null || lsCart.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
                return "redirect:/cart";
            }

            Address shippingAddress = addressRepository.findById(addressId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found."));

            Order order = new Order();
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setStatusId(1); // Pending
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setPayStatusId(1); // Unpaid

            order.setUser(userService.getUserById(customUserDetails.getUserId()));
            order.setOrderStatus(orderStatus);
            order.setPaymentStatus(paymentStatus);
            order.setAddress(shippingAddress);

            BigDecimal totalAmount = lsCart.stream()
                    .map(item -> BigDecimal.valueOf(item.getTotalPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(totalAmount);
            
            order.setOrderDate(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            Order savedOrder = orderService.save(order);

            for (CartItermDTO item : lsCart) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(savedOrder);
                orderDetail.setProduct(productService.getProductById(item.getProductId()));
                orderDetail.setQuantity(item.getQuantityCart());
                orderDetail.setPrice(BigDecimal.valueOf(item.getProductPrice()));
                orderDetailService.save(orderDetail);

                Product product = productService.getProductById(item.getProductId());
                product.setStockQuantity(product.getStockQuantity() - item.getQuantityCart());
                productService.save(product);
            }

            cartService.clearCart(response);

            // Thêm orderId vào Flash Attribute để gửi sang trang success
            redirectAttributes.addFlashAttribute("orderId", savedOrder.getOrderId());
            // Trả về một redirect URL
            return "redirect:/order-success";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to place order: " + e.getMessage());
            // Nếu có lỗi, quay lại trang checkout
            return "redirect:/cart/checkout";
        }
    }

    @GetMapping("/order-success")
    public String orderSuccess(Model model) {
        if (!model.containsAttribute("orderId")) {
            return "redirect:/";
        }
        return "client/order-success";
    }
}