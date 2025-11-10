package com.flowerShop1.controller;

import com.flowerShop1.dto.Order.OrderDTO;
import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.OrderDetail;
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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            @RequestParam(defaultValue = "orderDate") String sortBy, // Thay đổi sortBy mặc định
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Double minTotal,
            @RequestParam(required = false) Double maxTotal,
            Model model,
            RedirectAttributes ra) {

        int pageSize = 10;
        LocalDateTime from = (fromDate != null && !fromDate.isEmpty()) ? LocalDate.parse(fromDate).atStartOfDay() : null;
        LocalDateTime to = (toDate != null && !toDate.isEmpty()) ? LocalDate.parse(toDate).atTime(23, 59, 59) : null;

        // ✅ Các đoạn validate giữ nguyên...
        if (from != null && to != null && !to.isAfter(from)) {
            ra.addFlashAttribute("error", "❌ Lỗi date range: 'To date' phải sau 'From date'.");
            return "redirect:/manager/orders";
        }
        if (to != null && to.isAfter(LocalDateTime.now())) {
            ra.addFlashAttribute("error", "❌ Lỗi date range: 'To date' không thể chọn ngày tương lai.");
            return "redirect:/manager/orders";
        }
        if (minTotal != null && maxTotal != null && (minTotal <= 0 || minTotal > maxTotal)) {
            ra.addFlashAttribute("error", "❌ Lỗi total amount range:  0 < min ≤ max.");
            return "redirect:/manager/orders";
        }

        Page<Order> pageOrders = orderService.searchOrders(
                keyword, paymentMethod, statusId, sortBy, sortDir,
                PageRequest.of(page - 1, pageSize), from, to, minTotal, maxTotal
        );

        // ✅ TỐI ƯU HÓA: Lấy danh sách ID đơn hàng và truy vấn Payment một lần duy nhất
        List<Integer> orderIds = pageOrders.getContent().stream().map(Order::getOrderId).toList();
        List<Payment> payments = paymentRepository.findByOrder_OrderIdIn(orderIds);
        Map<Integer, String> paymentMethodMap = new HashMap<>();
        for (Payment p : payments) {
            // Chỉ lưu phương thức thanh toán đầu tiên tìm thấy cho mỗi orderId
            paymentMethodMap.putIfAbsent(p.getOrder().getOrderId(), p.getPayment_method());
        }

        // ✅ SỬ DỤNG PAGE.MAP() ĐỂ CHUYỂN ĐỔI SANG DTO HIỆU QUẢ HƠN
        Page<OrderDTO> orderDtoPage = pageOrders.map(order -> {
            OrderDTO dto = new OrderDTO(order); // Sử dụng constructor mới
            // Lấy payment method từ Map đã tạo, nếu không có thì mặc định là "COD"
            dto.setPaymentMethod(paymentMethodMap.getOrDefault(order.getOrderId(), "COD"));
            return dto;
        });

        // Thay vì truyền 'orders' và 'orderDTOs' riêng, chỉ cần truyền 'orderDtoPage'
        model.addAttribute("orderPage", orderDtoPage);

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

        // Thêm các thông báo lỗi và thành công (nếu có)
        if (model.containsAttribute("success")) {
            model.addAttribute("success", model.getAttribute("success"));
        }
        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }

        return "manager/orders/list";
    }

    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable Integer orderId, Model model) {

        Order order = orderService.getById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderDetail> items = orderService.getOrderDetails(orderId);
        Payment payment = orderService.getPaymentByOrderId(orderId);

        // Locale Việt Nam (chuẩn Java 21+)
        Locale localeVN = Locale.forLanguageTag("vi-VN");

        // ✅ Format ngày
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy 'lúc' hh:mm a", localeVN);
        String orderDateFormatted = order.getOrderDate().format(dateFormatter);

        // ✅ Format tiền
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        String orderTotalFormatted = currencyFormatter.format(order.getTotalAmount());

        // ✅ Tính tổng số lượng item
        int totalQuantity = items.stream().mapToInt(OrderDetail::getQuantity).sum();


        model.addAttribute("order", order);
        model.addAttribute("items", items);
        model.addAttribute("payment", payment);
        model.addAttribute("orderDateFormatted", orderDateFormatted);
        model.addAttribute("orderTotalFormatted", orderTotalFormatted);
        model.addAttribute("totalQuantity", totalQuantity);

        return "manager/orders/view";
    }

}
