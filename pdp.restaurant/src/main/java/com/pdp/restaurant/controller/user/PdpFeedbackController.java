package com.pdp.restaurant.controller.user;

import com.pdp.restaurant.entity.PdpFeedback;
import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.repository.PdpFeedbackRepository;
import com.pdp.restaurant.repository.PdpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PdpFeedbackController {

    private final PdpFeedbackRepository feedbackRepository;
    private final PdpUserRepository userRepository;

    @PostMapping("/feedback")
    public String sendFeedback(
            @RequestParam("content") String content,
            @RequestParam("rating") Integer rating
    ) {
        // Lấy email user đang đăng nhập
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // Tìm user theo email
        PdpUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo feedback
        PdpFeedback feedback = new PdpFeedback();
        feedback.setUser(user);
        feedback.setContent(content);
        feedback.setRating(rating);

        feedbackRepository.save(feedback);

        return "redirect:/";
    }
}
