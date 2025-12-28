package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpOrder;
import com.pdp.restaurant.service.PdpOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/review")
@RequiredArgsConstructor
public class PdpAdminReviewController {

    private final PdpOrderService orderService;

    // ===============================
    // LIST REVIEW (Lấy từ những đơn hàng đã đánh giá)
    // ===============================
    @GetMapping
    public String list(Model model) {
        // Lấy tất cả đơn hàng, sau đó lọc những đơn có rating khác null
        List<PdpOrder> reviews = orderService.findAll().stream()
                .filter(order -> order.getRating() != null)
                .collect(Collectors.toList());

        model.addAttribute("reviews", reviews);
        return "admin/review/list";
    }

    // ===============================
    // DETAIL REVIEW
    // ===============================
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PdpOrder order = orderService.findById(id);
        if (order == null || order.getRating() == null) {
            return "redirect:/admin/review";
        }

        model.addAttribute("review", order);
        return "admin/review/revew-detail";
    }

    // ===============================
    // DELETE REVIEW (Xóa đánh giá thực chất là set rating về null)
    // ===============================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        PdpOrder order = orderService.findById(id);
        if (order != null) {
            order.setRating(null);
            order.setReviewComment(null);
            order.setReviewedAt(null);
            orderService.save(order);
        }
        return "redirect:/admin/review";
    }
}