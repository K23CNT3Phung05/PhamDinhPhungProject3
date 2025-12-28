package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpOrder;
import com.pdp.restaurant.entity.PdpOrderStatus;
import com.pdp.restaurant.service.PdpOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class PdpAdminOrderController {

    private final PdpOrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        List<PdpOrder> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        // Thống kê số lượng để đẩy ra các Card Stats
        long totalOrders = (orders != null) ? orders.size() : 0;
        long pendingCount = 0;

        if (orders != null) {
            pendingCount = orders.stream()
                    .filter(o -> o.getOrderStatus() != null && o.getOrderStatus() == PdpOrderStatus.PENDING)
                    .count();
        }

        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingCount", pendingCount); // Gửi biến này ra HTML

        return "admin/orders/list";
    }

    @GetMapping("/{id}/detail")
    public String detailOrder(@PathVariable Long id, Model model) {
        PdpOrder order = orderService.findById(id);
        if (order == null) {
            return "redirect:/admin/orders?error=OrderNotFound";
        }
        model.addAttribute("order", order);
        return "admin/orders/order-details";
    }

    @PostMapping("/update")
    public String updateStatus(@RequestParam Long id,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        try {
            PdpOrderStatus newStatus = PdpOrderStatus.valueOf(status);
            orderService.updateStatus(id, newStatus);
            ra.addFlashAttribute("success", "Cập nhật thành công đơn hàng #" + id);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes ra) {
        try {
            orderService.delete(id);
            ra.addFlashAttribute("success", "Đã xóa đơn hàng #" + id);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa đơn hàng!");
        }
        return "redirect:/admin/orders";
    }
}