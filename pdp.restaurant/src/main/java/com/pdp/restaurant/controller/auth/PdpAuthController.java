package com.pdp.restaurant.controller.auth;

import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.service.PdpUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class PdpAuthController {

    private final PdpUserService userService;

    // ================= LOGIN FORM =================
    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            Model model) {

        if (error != null) model.addAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
        if (logout != null) model.addAttribute("successMessage", "Bạn đã đăng xuất thành công.");
        if (registered != null) model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");

        return "login";  // templates/login.html
    }

    // ================= LOGIN PROCESS =================
    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        PdpUser user = userService.login(email, password);

        if (user == null) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
            return "login";
        }

        // Lưu user vào session
        session.setAttribute("currentUser", user);

        // Admin → admin dashboard
        if ("ADMIN".equalsIgnoreCase(user.getRole().name())) {
            return "redirect:/admin/";
        }

        // User → home page
        return "redirect:/";
    }

    // ================= LOGOUT =================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // ================= REGISTER =================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new PdpUser());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute PdpUser user, Model model) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            model.addAttribute("errorMessage", "Email không được để trống!");
            return "register";
        }

        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("errorMessage", "Email đã được sử dụng!");
            return "register";
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            model.addAttribute("errorMessage", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "register";
        }

        user.setRole(PdpUser.Role.USER);  // Mặc định role là USER
        userService.registerUser(user);

        return "redirect:/login?registered";
    }
}
