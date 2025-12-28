package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpCart;
import com.pdp.restaurant.repository.PdpCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/carts")
@RequiredArgsConstructor
public class PdpAdminCartController {

    private final PdpCartRepository cartRepository;

    @GetMapping
    public String listCarts(Model model) {
        List<PdpCart> carts = cartRepository.findAll();

        // SỬA TẠI ĐÂY: Đổi tên thành cartItems để khớp với HTML dòng 32
        model.addAttribute("cartItems", carts != null ? carts : new java.util.ArrayList<>());

        return "admin/carts";
    }

    @GetMapping("/delete/{id}")
    public String deleteCart(@PathVariable Long id) {
        cartRepository.deleteById(id);
        return "redirect:/admin/carts";
    }
}