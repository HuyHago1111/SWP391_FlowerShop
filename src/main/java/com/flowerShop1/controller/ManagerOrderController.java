package com.flowerShop1.controller;

import com.flowerShop1.dto.Order.OrderDTO;
import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.OrderStatus;
import com.flowerShop1.entity.Payment;
import com.flowerShop1.repository.OrderStatusRepository;
import com.flowerShop1.repository.PaymentRepository;
import com.flowerShop1.repository.ShipperRepository;
import com.flowerShop1.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manager/orders")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN','STAFF')")
public class ManagerOrderController {
    @Autowired private OrderStatusRepository orderStatusRepository;
    @Autowired private OrderService orderService;
    @Autowired private ShipperRepository shipperRepository;
    @Autowired private PaymentRepository paymentRepository;
    @GetMapping("")
    public String listOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "all") String paymentMethod,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(defaultValue = "totalAmount") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Double minTotal,
            @RequestParam(required = false) Double maxTotal,
            @ModelAttribute("success") String success,
            @ModelAttribute("error") String error,
            Model model,
            RedirectAttributes ra) {

        int pageSize = 10;
        LocalDateTime from = (fromDate != null && !fromDate.isEmpty()) ? LocalDate.parse(fromDate).atStartOfDay() : null;
        LocalDateTime to = (toDate != null && !toDate.isEmpty()) ? LocalDate.parse(toDate).atTime(23, 59, 59) : null;

        // ✅ Validate khoảng ngày
        if (from != null && to != null && !to.isAfter(from)) {
            ra.addFlashAttribute("error", "❌ Lỗi date range: 'To date' phải sau 'From date'.");
            return "redirect:/manager/orders";
        }

        // ✅ Validate khoảng ngày không vượt quá hiện tại
        if (to != null && to.isAfter(LocalDateTime.now())) {
            ra.addFlashAttribute("error", "❌ Lỗi date range: 'To date' không thể chọn ngày tương lai.");
            return "redirect:/manager/orders";
        }

        // ✅ Validate tổng tiền
        if (minTotal != null && maxTotal != null && (minTotal <= 0 || minTotal > maxTotal)) {
            ra.addFlashAttribute("error", "❌ Lỗi total amount range:  0 < min ≤ max.");
            return "redirect:/manager/orders";
        }

        Page<Order> pageOrders = orderService.searchOrders(
                keyword, paymentMethod, statusId, sortBy, sortDir,
                PageRequest.of(page - 1, pageSize), from, to, minTotal, maxTotal
        );
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : pageOrders.getContent()) {
            OrderDTO dto = new OrderDTO();
            Payment payment = paymentRepository.findByOrderId(order.getOrderId());
            dto.setPaymentMethod(payment != null ? payment.getPayment_method() : "N/A");
            orderDTOs.add(dto);
        }

        model.addAttribute("orders", pageOrders.getContent());
        model.addAttribute("orderDTOs", orderDTOs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageOrders.getTotalPages());
        model.addAttribute("statuses", orderStatusRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("statusId", statusId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("minTotal", minTotal);
        model.addAttribute("maxTotal", maxTotal);

        return "manager/orders/list";
    }

    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable Integer orderId, Model model) {
        Order order = orderService.getById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "manager/orders/view";
    }

    @GetMapping("/{orderId}/tracking")
    public String trackingOrder(@PathVariable Integer orderId, Model model) {
        Order order = orderService.getById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderStatus> statuses = orderService.getAllStatuses();
        model.addAttribute("order", order);
        model.addAttribute("statuses", statuses);
        model.addAttribute("shippers", shipperRepository.findAll());
        return "manager/orders/tracking";
    }

    @PostMapping("/{orderId}/update-status")
    public String updateStatus(@PathVariable Integer orderId, @RequestParam Integer statusId, RedirectAttributes ra) {
        orderService.updateOrderStatus(orderId, statusId);
        ra.addFlashAttribute("success", "✅ Order status updated successfully!");
        return "redirect:/manager/orders/" + orderId + "/tracking";
    }

    @PostMapping("/{orderId}/assign-shipper")
    public String assignShipper(@PathVariable Integer orderId, @RequestParam Integer shipperId, RedirectAttributes ra) {
        orderService.assignShipper(orderId, shipperId);
        ra.addFlashAttribute("success", "🚚 Shipper assigned successfully!");
        return "redirect:/manager/orders/" + orderId + "/tracking";
    }
}
