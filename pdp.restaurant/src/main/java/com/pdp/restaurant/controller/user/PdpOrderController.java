package com.pdp.restaurant.controller.user;

import com.pdp.restaurant.entity.PdpDish;
import com.pdp.restaurant.entity.PdpOrder;
import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.service.PdpCartService;
import com.pdp.restaurant.service.PdpOrderService;
import com.pdp.restaurant.service.PdpDishService;
import com.pdp.restaurant.service.PdpUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class PdpOrderController {

    private final PdpCartService cartService;
    private final PdpOrderService orderService;
    private final PdpDishService dishService;
    private final PdpUserService userService;

    // ================= 1. VÀO THẲNG THỰC ĐƠN =================
    @GetMapping({"", "/"})
    public String index() {
        return "redirect:/dishs";
    }

    // ================= 2. ĐẶT NGAY TỪ TRANG DISHS =================
    @GetMapping("/buy-now")
    public String buyNow(@RequestParam("dishId") Long dishId, HttpSession session) {
        PdpDish dish = dishService.findById(dishId);
        if (dish != null) {
            cartService.clearCart(session);
            cartService.addToCart(dish, session);
            return "redirect:/orders/checkout";
        }
        return "redirect:/dishs";
    }

    // ================= 3. TRANG THANH TOÁN (Yêu cầu Login) =================
    @GetMapping("/checkout")
    public String checkoutPage(Model model, HttpSession session, Principal principal) {
        if (principal == null) return "redirect:/login";

        var cartItems = cartService.getCartItems(session);
        if (cartItems == null || cartItems.isEmpty()) return "redirect:/dishs";

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.getTotalPrice(session));
        return "orders/order-details";
    }

    // ================= 4. XỬ LÝ TẠO ĐƠN HÀNG (Gán đúng User) =================
    @PostMapping("/create")
    public String createOrder(
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("shippingAddress") String shippingAddress,
            @RequestParam(value = "note", required = false) String note,
            Principal principal,
            HttpSession session,
            RedirectAttributes ra
    ) {
        if (principal == null) return "redirect:/login";

        var cart = cartService.getCart(session);
        if (cart == null || cart.isEmpty()) return "redirect:/dishs";

        try {
            String currentUserEmail = principal.getName();
            orderService.checkout(fullName, phone, shippingAddress, note,
                    java.math.BigDecimal.ZERO, currentUserEmail, cart);

            cartService.clearCart(session);
            ra.addFlashAttribute("successMessage", "🎉 Đơn hàng đã được tiếp nhận thành công!");
            return "redirect:/orders/list";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi đặt hàng: " + e.getMessage());
            return "redirect:/orders/checkout";
        }
    }

    // ================= 5. LỊCH SỬ ĐƠN HÀNG RIÊNG TƯ =================
    @GetMapping("/list")
    public String orderList(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        PdpUser currentUser = userService.getUserByEmail(principal.getName()).orElse(null);
        if (currentUser == null) return "redirect:/login";

        List<PdpOrder> orders = orderService.findByUser(currentUser);
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    // ================= 6. XỬ LÝ ĐÁNH GIÁ (Sửa lỗi 404 Review) =================
    @PostMapping("/{id}/review")
    public String submitReview(
            @PathVariable Long id,
            @RequestParam("rating") Integer rating,
            @RequestParam(value = "comment", required = false) String comment,
            Principal principal,
            RedirectAttributes ra
    ) {
        if (principal == null) return "redirect:/login";

        try {
            PdpOrder order = orderService.findById(id);
            // Bảo mật: Chỉ chủ nhân đơn hàng mới được đánh giá
            if (order != null && order.getUser().getEmail().equals(principal.getName())) {
                order.setRating(rating);
                order.setReviewComment(comment);
                order.setReviewedAt(LocalDateTime.now());

                orderService.save(order); // Lưu lại đánh giá vào DB
                ra.addFlashAttribute("successMessage", "🌟 Cảm ơn bạn đã đánh giá!");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi gửi đánh giá: " + e.getMessage());
        }
        return "redirect:/orders/list";
    }

    // ================= 7. CẬP NHẬT THÔNG TIN =================
    @PostMapping("/{id}/update-info")
    public String updateOrderInfo(
            @PathVariable Long id,
            @RequestParam("customerName") String name,
            @RequestParam("customerPhone") String phone,
            @RequestParam("customerAddress") String address,
            Principal principal,
            RedirectAttributes ra
    ) {
        PdpOrder order = orderService.findById(id);

        if (order == null || principal == null || !order.getUser().getEmail().equals(principal.getName())) {
            ra.addFlashAttribute("errorMessage", "Hành động không hợp lệ!");
            return "redirect:/orders/list";
        }

        if ("PENDING".equalsIgnoreCase(order.getOrderStatus().name())) {
            order.setCustomerName(name);
            order.setCustomerPhone(phone);
            order.setCustomerAddress(address);
            orderService.save(order);
            ra.addFlashAttribute("successMessage", "Cập nhật thành công!");
        } else {
            ra.addFlashAttribute("errorMessage", "Không thể sửa đơn hàng này.");
        }
        return "redirect:/orders/list";
    }
}