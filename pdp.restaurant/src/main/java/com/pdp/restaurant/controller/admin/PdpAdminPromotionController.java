package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpPromotion;
import com.pdp.restaurant.repository.PdpPromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/promotions")
@RequiredArgsConstructor
public class PdpAdminPromotionController {

    private final PdpPromotionRepository repo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("promotions", repo.findAll());
        return "admin/promotion/list";
    }

    @GetMapping("/create")
    public String form(Model model) {
        model.addAttribute("promotion", new PdpPromotion());
        return "admin/promotion/form";
    }

    @PostMapping("/save")
    public String save(PdpPromotion promotion) {
        repo.save(promotion);
        return "redirect:/admin/promotions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/admin/promotions";
    }
}
