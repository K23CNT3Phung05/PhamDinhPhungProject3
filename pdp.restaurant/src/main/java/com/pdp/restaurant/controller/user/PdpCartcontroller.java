package com.pdp.restaurant.controller.user;

import com.pdp.restaurant.repository.PdpDishRepository;
import com.pdp.restaurant.service.PdpCartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart") // Tất cả đường dẫn đều bắt đầu bằng /cart/...
public class PdpCartController {

    private final PdpDishRepository dishRepo;
    private final PdpCartService cartService;

    // --- 1. CÁC HÀM XỬ LÝ GIỎ HÀNG (Dùng cho trang Dishs & Cart) ---

    @PostMapping("/add")
    public String addToCart(@RequestParam Long dishId, HttpSession session) {
        cartService.addToCart(
                dishRepo.findById(dishId).orElseThrow(() -> new RuntimeException("Không tìm thấy món")),
                session
        );
        return "redirect:/cart";
    }

    @PostMapping("/quick-order")
    public String quickOrder(@RequestParam Long dishId, HttpSession session) {
        cartService.addToCart(
                dishRepo.findById(dishId).orElseThrow(() -> new RuntimeException("Món ăn không tồn tại")),
                session
        );
        // Sau khi thêm vào giỏ, chuyển thẳng đến trang nhập thông tin thanh toán
        return "redirect:/cart/checkout";
    }

    // --- 2. CÁC HÀM HIỂN THỊ GIAO DIỆN (GET) ---

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cartItems", cartService.getCartItems(session));
        model.addAttribute("totalPrice", cartService.getTotalPrice(session));
        return "cart"; // Mở file templates/cart.html
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        var items = cartService.getCartItems(session);
        if (items == null || items.isEmpty()) {
            return "redirect:/dishs";
        }
        model.addAttribute("cartItems", items);
        model.addAttribute("totalPrice", cartService.getTotalPrice(session));
        return "checkout"; // Mở file templates/checkout.html
    }

    // --- 3. HÀM XỬ LÝ ĐẶT HÀNG (POST) ---
    // Hàm này dùng chung cho cả Form ở cart.html và checkout.html
    @PostMapping("/process-checkout")
    public String processCheckout(HttpSession session,
                                  @RequestParam String customerName,
                                  @RequestParam String customerPhone,
                                  @RequestParam String customerAddress,
                                  RedirectAttributes ra) {

        var items = cartService.getCartItems(session);
        if (items == null || items.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Giỏ hàng của bạn đang trống!");
            return "redirect:/cart";
        }

        // Ghi log kiểm tra dữ liệu khách nhập
        System.out.println("--- ĐƠN HÀNG MỚI NHẬN ---");
        System.out.println("Khách hàng: " + customerName);
        System.out.println("Điện thoại: " + customerPhone);
        System.out.println("Địa chỉ: " + customerAddress);

        // Xoá giỏ hàng sau khi đặt thành công
        cartService.clearCart(session);

        // Báo thành công về trang món ăn (Trang dishs của bạn sẽ hiện Alert này)
        ra.addFlashAttribute("successMessage", "Đặt hàng thành công! Chúng tôi sẽ liên hệ với bạn ngay.");
        return "redirect:/dishs";
    }

    // --- 4. CÁC TIỆN ÍCH CẬP NHẬT GIỎ HÀNG ---

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long dishId, @RequestParam int quantity, HttpSession session) {
        cartService.updateQuantity(dishId, quantity, session);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long dishId, HttpSession session) {
        cartService.removeItem(dishId, session);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }
}