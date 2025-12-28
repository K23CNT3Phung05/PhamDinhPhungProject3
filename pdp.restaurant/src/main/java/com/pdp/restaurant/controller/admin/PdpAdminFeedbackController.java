package com.pdp.restaurant.controller.admin;

import com.pdp.restaurant.entity.PdpFeedback;
import com.pdp.restaurant.repository.PdpFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/feedback")
@RequiredArgsConstructor
public class PdpAdminFeedbackController {

    private final PdpFeedbackRepository feedbackRepository;

    // =========================
    // GET ALL FEEDBACK
    // =========================
    @GetMapping
    public List<PdpFeedback> getAll() {
        return feedbackRepository.findAll();
    }

    // =========================
    // GET FEEDBACK BY ID
    // =========================
    @GetMapping("/{id}")
    public PdpFeedback getById(@PathVariable Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    // =========================
    // CREATE FEEDBACK
    // =========================
    @PostMapping
    public PdpFeedback create(@RequestBody PdpFeedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // =========================
    // UPDATE FEEDBACK
    // =========================
    @PutMapping("/{id}")
    public PdpFeedback update(@PathVariable Long id,
                              @RequestBody PdpFeedback feedback) {

        PdpFeedback old = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        // 🔥 CHỈ UPDATE NHỮNG FIELD TỒN TẠI
        old.setContent(feedback.getContent());
        old.setRating(feedback.getRating());

        return feedbackRepository.save(old);
    }

    // =========================
    // DELETE FEEDBACK
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        feedbackRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
