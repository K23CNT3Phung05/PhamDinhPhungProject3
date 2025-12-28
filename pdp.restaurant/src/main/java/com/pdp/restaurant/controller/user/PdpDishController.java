package com.pdp.restaurant.controller.user;

import com.pdp.restaurant.entity.PdpDish;
import com.pdp.restaurant.service.PdpDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/dishs")
@RequiredArgsConstructor
public class PdpDishController {

    private final PdpDishService dishService;

    @GetMapping
    public String showDishs(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String category,
            Model model) {

        List<PdpDish> results;

        // 1. Tìm kiếm theo tên món ăn
        if (name != null && !name.trim().isEmpty()) {
            results = dishService.searchByName(name.trim());
            model.addAttribute("keyword", name.trim());
            model.addAttribute("selectedCategory", null);
        }

        else if (category != null && !category.trim().isEmpty()) {

            results = dishService.getByCategoryName(category.trim());
            model.addAttribute("selectedCategory", category.trim());
            model.addAttribute("keyword", null);
        }
        // 3. Hiển thị tất cả
        else {
            results = dishService.getAll();
            model.addAttribute("selectedCategory", null);
            model.addAttribute("keyword", null);
        }

        model.addAttribute("dishs", results);
        return "dishs";
    }

    @GetMapping("/{id:\\d+}")
    public String dishDetail(@PathVariable Long id, Model model) {
        PdpDish dish = dishService.getById(id);
        model.addAttribute("dish", dish);
        return "dishs-detail";
    }
}