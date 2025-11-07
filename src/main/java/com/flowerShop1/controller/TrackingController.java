package com.flowerShop1.controller;

import com.flowerShop1.entity.*;
import com.flowerShop1.repository.OrderStatusRepository;
import com.flowerShop1.repository.ShipperRepository;
import com.flowerShop1.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/manager/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER','ADMIN','STAFF')")
public class TrackingController {

    private final OrderService orderService;
    private final OrderStatusRepository orderStatusRepository;
    private final ShipperRepository shipperRepository;

    @GetMapping("/{id}/tracking")
    public String viewTracking(@PathVariable Integer id, Model model,
                               @ModelAttribute("success") String success,
                               @ModelAttribute("error") String error) {

        // ✅ Kiểm tra null tránh NPE
        if (id == null) {
            model.addAttribute("error", "❌ Order ID không hợp lệ!");
            return "redirect:/manager/orders";
        }

        Order order = orderService.getOrderWithRelations(id);
        if (order == null) {
            model.addAttribute("error", "❌ Không tìm thấy đơn hàng #" + id);
            return "redirect:/manager/orders";
        }

        String paymentMethod = order.getPaymentStatus().getPayStatusName();

        List<OrderStatus> availableStatuses;

        // ✅ Dải trạng thái hợp lệ theo Payment Method
        if (paymentMethod.equalsIgnoreCase("VNPay")) {
            availableStatuses = orderStatusRepository.findBystatusIdBetween(3, 6);
        } else {
            availableStatuses = orderStatusRepository.findBystatusIdBetween(1, 6);
        }

        // ✅ Locale Việt Nam chuẩn (Java 17+)
        Locale localeVN = Locale.forLanguageTag("vi-VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy 'lúc' HH:mm", localeVN);

        // ✅ Format dữ liệu hiển thị
        String totalFormatted = currencyFormatter.format(order.getTotalAmount());
        String orderDateFormatted = order.getOrderDate() != null
                ? order.getOrderDate().format(dateFormatter)
                : "Chưa có";

        model.addAttribute("order", order);
        model.addAttribute("availableStatuses", availableStatuses);
        model.addAttribute("shippers", shipperRepository.findAllActiveShippers());
        model.addAttribute("orderDateFormatted", orderDateFormatted);
        model.addAttribute("totalFormatted", totalFormatted);

        // ✅ Flash messages
        if (success != null && !success.isEmpty())
            model.addAttribute("success", success);
        if (error != null && !error.isEmpty())
            model.addAttribute("error", error);

        return "manager/orders/tracking";
    }

    @PostMapping("/{id}/tracking/update")
    public String updateTracking(@PathVariable Integer id,
                                 @RequestParam("newStatusId") Integer newStatusId,
                                 @RequestParam(value = "shipperId", required = false) Integer shipperId,
                                 RedirectAttributes ra) {

        if (id == null) {
            ra.addFlashAttribute("error", "❌ Order ID không hợp lệ!");
            return "redirect:/manager/orders";
        }

        Order order = orderService.getOrderWithRelations(id);
        if (order == null) {
            ra.addFlashAttribute("error", "❌ Không tìm thấy đơn hàng #" + id);
            return "redirect:/manager/orders";
        }

        int currentStatus = order.getOrderStatus().getStatusId();

        boolean statusChanged = (newStatusId != null && newStatusId != currentStatus);

        // ✅ Nếu đơn hàng đã >= 3 (“Đang chuẩn bị”), KHÔNG cho đổi trạng thái nhưng vẫn cho đổi shipper
        if (currentStatus >= 3 && statusChanged) {
            ra.addFlashAttribute("error", "⚠️ Đơn hàng đã ở trạng thái 'Đang chuẩn bị' hoặc cao hơn, không thể cập nhật tiến trình!");
            return "redirect:/manager/orders/" + id + "/tracking";
        }

        // ✅ Chỉ validate thứ tự nếu người dùng thực sự đổi trạng thái
        if (statusChanged) {
            if (newStatusId <= currentStatus) {
                ra.addFlashAttribute("error", "❌ Không thể quay lại trạng thái trước đó!");
                return "redirect:/manager/orders/" + id + "/tracking";
            }

            if (newStatusId > currentStatus + 1) {
                ra.addFlashAttribute("error", "⚠️ Cập nhật phải theo thứ tự từng bước!");
                return "redirect:/manager/orders/" + id + "/tracking";
            }

            // Cập nhật trạng thái mới
            OrderStatus newStatus = new OrderStatus();
            newStatus.setStatusId(newStatusId);
            order.setOrderStatus(newStatus);
        }

        // ✅ Cho phép cập nhật shipper độc lập
        if ( currentStatus < 4 ) {
            Shipper shipper = new Shipper();
            shipper.setShipperId(shipperId);
            order.setShipper(shipper);
        }else {
            if (shipperId != null) {
                ra.addFlashAttribute("error", "⚠️ Không thể thay đổi shipper khi đơn hàng đã ở trạng thái 'Đang giao hàng' hoặc cao hơn!");
                return "redirect:/manager/orders/" + id + "/tracking";
            }
        }

        // ✅ Cập nhật thời gian sửa đổi
        order.setUpdatedAt(LocalDateTime.now());
        orderService.save(order);

        if (statusChanged) {
            ra.addFlashAttribute("success", "✅ Cập nhật trạng thái thành công!");
        } else {
            ra.addFlashAttribute("success", "✅ Đã phân công hoặc thay đổi shipper thành công!");
        }
        return "redirect:/manager/orders/" + id + "/tracking";
    }
}

