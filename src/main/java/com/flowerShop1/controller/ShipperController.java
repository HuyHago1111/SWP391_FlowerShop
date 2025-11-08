package com.flowerShop1.controller;

import com.flowerShop1.entity.*;
import com.flowerShop1.repository.*;
import com.flowerShop1.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/shipper/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('Shipper')") // ✅ Đồng bộ với SecurityConfig
public class ShipperController {

    @Autowired private final OrderStatusRepository orderStatusRepository;
    @Autowired private final OrderService orderService;
    @Autowired private final PaymentStatusRepository paymentStatusRepository;
    @Autowired private final PaymentRepository paymentRepository;
    @Autowired private final ShipperRepository shipperRepository;

    // ✅ Trang 1: Danh sách đơn hàng được giao
    @GetMapping("")
    public String viewOrdersAssignedToShipper(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
            @RequestParam(value = "statusId", required = false) Integer statusId,
            @RequestParam(value = "sortField", required = false, defaultValue = "orderDate") String sortField,
            @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            Model model,
            Authentication auth) {

        Integer shipperId = getCurrentShipperId(auth);
        if (shipperId == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        List<Order> orders = orderService.findOrdersForShipperWithFilter(shipperId, search, paymentMethod, statusId, sortField, sortDir, page, size);

        Locale localeVN = Locale.forLanguageTag("vi-VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", localeVN);

        model.addAttribute("orders", orders);
        model.addAttribute("search", search);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("statusId", statusId);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currencyFormatter", currencyFormatter);
        model.addAttribute("dateFormatter", dateFormatter);

        return "shipper/list";
    }

    // ✅ Trang 2: Xem chi tiết đơn hàng (Order Detail)
    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable Integer id, Model model, Authentication auth) {
        Integer shipperId = getCurrentShipperId(auth);
        Order order = orderService.findByIdWithAllRelations(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (order.getShipper() == null || order.getShipper().getShipperId() != shipperId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem đơn hàng này!");
        }

        Locale localeVN = Locale.forLanguageTag("vi-VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        model.addAttribute("currencyFormatter", currencyFormatter);
        model.addAttribute("order", order);
        model.addAttribute("details", orderService.getOrderDetails(id));
        return "shipper/view";
    }

    // ✅ Trang 3: View Tracking Order (progress timeline)
    @GetMapping("/{id}/tracking")
    public String viewTracking(@PathVariable int id, Model model,
                               @ModelAttribute("success") String success,
                               @ModelAttribute("error") String error,
                               Authentication auth) {
        Integer shipperId = getCurrentShipperId(auth);
        Order order = orderService.findByIdWithAllRelations(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (order.getShipper() == null || !Objects.equals(order.getShipper().getShipperId(), shipperId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền theo dõi đơn này!");
        }

        // ✅ Lấy PaymentMethod thật từ bảng Payments
        Payment payment = paymentRepository.findTopByOrderIdOrderByPaymentDateDesc(id);
        String paymentMethod = (payment != null) ? payment.getPayment_method() : "COD";

        List<OrderStatus> availableStatuses = orderStatusRepository.findBystatusIdBetween(3, 6);
        Locale localeVN = Locale.forLanguageTag("vi-VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", localeVN);

        model.addAttribute("order", order);
        model.addAttribute("availableStatuses", availableStatuses);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("orderDateFormatted", order.getOrderDate().format(dateFormatter));
        model.addAttribute("totalFormatted", currencyFormatter.format(order.getTotalAmount()));
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "shipper/tracking";
    }

    // ✅ Cập nhật trạng thái (Tracking Update)
    @PostMapping("/{id}/tracking/update")
    public String updateTracking(
            @PathVariable int id,
            @RequestParam("newStatusId") int newStatusId,
            @RequestParam(value = "note", required = false) String note,
            RedirectAttributes ra,
            Authentication auth) {

        Integer shipperId = getCurrentShipperId(auth);
        Order order = orderService.findByIdWithAllRelations(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (order.getShipper() == null || !Objects.equals(order.getShipper().getShipperId(), shipperId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật đơn này!");
        }

        int currentStatus = order.getOrderStatus().getStatusId();

        // ⚠️ Validate: Shipper chỉ update từ 3 → 6
        if (newStatusId < 3 || newStatusId > 6) {
            ra.addFlashAttribute("error", "⚠️ Bạn chỉ được cập nhật trạng thái từ 3 → 6!");
            return "redirect:/shipper/orders/" + id + "/tracking";
        }

        if (newStatusId <= currentStatus) {
            ra.addFlashAttribute("error", "⚠️ Không thể quay lại trạng thái trước đó!");
            return "redirect:/shipper/orders/" + id + "/tracking";
        }

        if (newStatusId > currentStatus + 1) {
            ra.addFlashAttribute("error", "⚠️ Phải cập nhật theo từng bước!");
            return "redirect:/shipper/orders/" + id + "/tracking";
        }

        // 🧠 Nếu chọn "Thất bại" → note bắt buộc
        final int FAILURE_ID = 6; // ⚙️ chỉnh ID nếu cần
        OrderStatus newStatus = new OrderStatus();
        newStatus.setStatusId(newStatusId);
        order.setOrderStatus(newStatus);

        if (newStatusId == FAILURE_ID) {
            if (note == null || note.trim().isEmpty()) {
                ra.addFlashAttribute("error", "❌ Bạn phải ghi lý do thất bại vào Note!");
                return "redirect:/shipper/orders/" + id + "/tracking";
            }
            order.setNote(note.trim());

            // Nếu PaymentMethod = COD => cập nhật PaymentStatus = "Thanh toán thất bại"
            Payment payment = paymentRepository.findTopByOrderIdOrderByPaymentDateDesc(id);
            if (payment != null && "COD".equalsIgnoreCase(payment.getPayment_method())) {
                PaymentStatus failed = paymentStatusRepository.findByPayStatusName("Thanh toán thất bại");
                if (failed != null) order.setPaymentStatus(failed);
            }
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderService.save(order);
        ra.addFlashAttribute("success", "✅ Cập nhật trạng thái đơn hàng thành công!");
        return "redirect:/shipper/orders/" + id + "/tracking";
    }

    // 🔐 Helper: Lấy shipperId từ Authentication
    private Integer getCurrentShipperId(Authentication auth) {
        if (auth == null || auth.getName() == null) return null;
        String email = auth.getName();
        return shipperRepository.findByUser_email(email)
                .map(Shipper::getShipperId)
                .orElse(null);
    }
}
