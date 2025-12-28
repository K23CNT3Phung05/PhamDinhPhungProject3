package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpBanner;
import com.pdp.restaurant.repository.PdpBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
public class PdpAdminBannerController {

    private final PdpBannerRepository repo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("banners", repo.findAll());
        return "admin/banner/list";
    }

    @GetMapping("/create")
    public String form(Model model) {
        model.addAttribute("banner", new PdpBanner());
        return "admin/banner/form";
    }

    @PostMapping("/save")
    public String save(PdpBanner banner) {
        repo.save(banner);
        return "redirect:/admin/banners";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/admin/banners";
    }
}
