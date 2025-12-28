package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.repository.PdpUserRepository;
import com.pdp.restaurant.repository.PdpOrderRepository;
import com.pdp.restaurant.repository.PdpDishRepository;
import com.pdp.restaurant.repository.PdpReservationRepository; // Repository cho pdp_reservations
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PdpAdminController {

    private final PdpUserRepository userRepository;
    private final PdpOrderRepository orderRepository;
    private final PdpDishRepository dishRepository;
    private final PdpReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    // Lấy User đang đăng nhập hệ thống
    private PdpUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }

    // ==========================================
    // DASHBOARD CHÍNH (Tiền tự nhảy dựa trên total_amount)
    // ==========================================
    @GetMapping("/admin")
    public String adminIndex(Model model) {
        PdpUser admin = getCurrentUser();
        model.addAttribute("currentUser", admin);

        // 1. Lấy dữ liệu doanh thu tháng từ Database (Chỉ tính các đơn PAID)
        Double revenueMonth = orderRepository.sumMonthlyRevenue();
        double revenueVal = (revenueMonth != null) ? revenueMonth : 0.0;

        // Định dạng tiền tệ: ví dụ 1.220.000₫
        String formattedRevenue = String.format("%,.0f₫", revenueVal);
        model.addAttribute("totalRevenueMonth", formattedRevenue);

        // 2. Doanh thu hôm nay
        Double revenueToday = orderRepository.sumTodayRevenue();
        model.addAttribute("revenueToday", String.format("%,.0f₫", revenueToday != null ? revenueToday : 0.0));

        // 3. Đếm các thông số thực tế từ các bảng liên quan
        model.addAttribute("totalOrders", orderRepository.countCompletedOrders());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalDishs", dishRepository.count());
        model.addAttribute("totalReservations", reservationRepository.count());

        // 4. Dữ liệu biểu đồ doanh thu 7 ngày qua cho Chart.js
        List<Double> chartData = orderRepository.getRevenueLast7Days();
        if (chartData == null || chartData.isEmpty()) {
            chartData = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        }
        model.addAttribute("chartData", chartData);

        // 5. Cột thông số phụ bên phải Dashboard
        model.addAttribute("revenueWeek", formattedRevenue);
        model.addAttribute("totalCarts", 0); // Có thể bổ sung đếm từ pdp_cart nếu cần

        return "admin/index";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "redirect:/admin";
    }

    // ==========================================
    // QUẢN LÝ NGƯỜI DÙNG (TABLE: pdp_user)
    // ==========================================
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", new PdpUser());
        return "admin/users";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(@ModelAttribute PdpUser user) {
        if (user.getId() != null) {
            PdpUser existing = userRepository.findById(user.getId()).orElse(null);
            if (existing != null) {
                // Giữ mật khẩu cũ nếu không thay đổi
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    user.setPassword(existing.getPassword());
                } else {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            }
        } else {
            // Thêm mới user: Bắt buộc mã hóa mật khẩu
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // ==========================================
    // HỒ SƠ CÁ NHÂN (PROFILE)
    // ==========================================
    @GetMapping("/profile")
    public String showProfile(Model model) {
        PdpUser user = getCurrentUser();
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") PdpUser formData, Model model) {
        PdpUser currentUser = getCurrentUser();
        if (currentUser == null) return "redirect:/login";

        currentUser.setFullName(formData.getFullName());
        currentUser.setPhone(formData.getPhone());
        currentUser.setAddress(formData.getAddress());

        userRepository.save(currentUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        return "profile";
    }
}