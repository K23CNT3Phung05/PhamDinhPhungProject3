package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpCategory;
import com.pdp.restaurant.entity.PdpDish;
import com.pdp.restaurant.service.PdpCategoryService;
import com.pdp.restaurant.service.PdpDishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/dishs")
public class PdpAdminDishController {

    private final PdpDishService dishService;
    private final PdpCategoryService categoryService;

    public PdpAdminDishController(PdpDishService dishService, PdpCategoryService categoryService) {
        this.dishService = dishService;
        this.categoryService = categoryService;
    }

    // 1. Danh sách món ăn
    @GetMapping({"", "/"})
    public String listDishes(Model model) {
        List<PdpDish> dishes = dishService.getAll();
        model.addAttribute("dishes", dishes);
        return "admin/dishs/list";
    }

    // 2. Form thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("dish", new PdpDish());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/dishs/form";
    }

    // 3. Form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        PdpDish dish = dishService.getById(id);
        model.addAttribute("dish", dish);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/dishs/form";
    }

    // 4. Lưu dữ liệu (Add & Update)
    @PostMapping("/save")
    public String saveDish(@ModelAttribute PdpDish dish,
                           @RequestParam("categoryId") Long categoryId) {
        // Gán category cho dish
        PdpCategory category = categoryService.getById(categoryId);
        dish.setCategory(category);

        // Lưu món ăn
        dishService.save(dish);
        return "redirect:/admin/dishs";
    }

    // 5. Xóa món
    @GetMapping("/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        dishService.delete(id);
        return "redirect:/admin/dishs";
    }
}
