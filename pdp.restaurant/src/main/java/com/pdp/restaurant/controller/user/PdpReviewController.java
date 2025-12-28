package com.pdp.restaurant.controller.user;

import com.pdp.restaurant.entity.PdpReview;
import com.pdp.restaurant.repository.PdpDishRepository;
import com.pdp.restaurant.repository.PdpReviewRepository;
import com.pdp.restaurant.repository.PdpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PdpReviewController {

    private final PdpReviewRepository reviewRepo;
    private final PdpUserRepository userRepo;
    private final PdpDishRepository dishRepo;

    @PostMapping("/review")
    public String review(Long dishId, int rating, String comment) {

        // Lấy email của user đang đăng nhập
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        var dish = dishRepo.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + dishId));

        PdpReview r = new PdpReview();
        r.setUser(user);
        r.setDish(dish);
        r.setRating(rating);
        r.setComment(comment);

        reviewRepo.save(r);

        return "redirect:/";
    }
}
