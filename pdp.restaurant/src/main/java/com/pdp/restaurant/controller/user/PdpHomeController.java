package com.pdp.restaurant.controller.user;

import com.pdp.restaurant.entity.PdpDish;
import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.service.PdpDishService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PdpHomeController {

    private final PdpDishService dishService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        List<PdpDish> featuredDishes = dishService.getFeaturedDishes();
        model.addAttribute("featuredDishes", featuredDishes);
        model.addAttribute("pageTitle", "Trang chủ");

        PdpUser currentUser = (PdpUser) session.getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
        }

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "Về chúng tôi");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Liên hệ");
        return "contact";
    }
}
