package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpCategory;
import com.pdp.restaurant.repository.PdpCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class PdpAdminCategoryController {

    private final PdpCategoryRepository repo;

    // ===============================
    // LIST
    // ===============================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", repo.findAll());
        return "admin/categories/list"; // ✅ SỬA CHUẨN
    }

    // ===============================
    // CREATE FORM
    // ===============================
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category", new PdpCategory());
        return "admin/categories/form";
    }

    // ===============================
    // EDIT FORM
    // ===============================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {

        return repo.findById(id)
                .map(c -> {
                    model.addAttribute("category", c);
                    return "admin/categories/form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Danh mục không tồn tại!");
                    return "redirect:/admin/categories";
                });
    }

    // ===============================
    // SAVE (CREATE + UPDATE)
    // ===============================
    @PostMapping("/save")
    public String save(
            @ModelAttribute("category") PdpCategory c,
            RedirectAttributes ra
    ) {
        repo.save(c);
        ra.addFlashAttribute("success", "Lưu danh mục thành công!");
        return "redirect:/admin/categories";
    }

    // ===============================
    // DELETE
    // ===============================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {

        if (!repo.existsById(id)) {
            ra.addFlashAttribute("error", "Danh mục không tồn tại!");
        } else {
            repo.deleteById(id);
            ra.addFlashAttribute("success", "Xóa danh mục thành công!");
        }

        return "redirect:/admin/categories";
    }
}
