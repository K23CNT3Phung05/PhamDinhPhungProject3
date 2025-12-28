package com.pdp.restaurant.service;

import com.pdp.restaurant.entity.PdpDish;
import com.pdp.restaurant.entity.PdpReview;
import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.repository.PdpDishRepository;
import com.pdp.restaurant.repository.PdpReviewRepository;
import com.pdp.restaurant.repository.PdpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PdpReviewService {

    private final PdpReviewRepository reviewRepository;
    private final PdpUserRepository userRepository;
    private final PdpDishRepository dishRepository;

    /**
     * Lấy tất cả review
     */
    public List<PdpReview> getAll() {
        return reviewRepository.findAll();
    }

    /**
     * Lấy review theo id
     */
    public PdpReview getById(Long id) {
        Optional<PdpReview> reviewOpt = reviewRepository.findById(id);
        return reviewOpt.orElseThrow(() -> new RuntimeException("Review not found"));
    }

    /**
     * Tạo hoặc cập nhật review
     */
    public PdpReview save(PdpReview review) {
        // Kiểm tra user
        if (review.getUser() != null && review.getUser().getId() != null) {
            PdpUser user = userRepository.findById(review.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            review.setUser(user);
        }

        // Kiểm tra dish
        if (review.getDish() != null && review.getDish().getId() != null) {
            PdpDish dish = dishRepository.findById(review.getDish().getId())
                    .orElseThrow(() -> new RuntimeException("Dish not found"));
            review.setDish(dish);
        }

        // Lưu review (save trả về entity, không phải Optional)
        return reviewRepository.save(review);
    }

    /**
     * Xóa review theo id
     */
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    /**
     * Lấy tất cả review của một món ăn
     */
    public List<PdpReview> getByDish(Long dishId) {
        PdpDish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        return reviewRepository.findByDish(dish);
    }

    /**
     * Lấy tất cả review của một user
     */
    public List<PdpReview> getByUser(Long userId) {
        PdpUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reviewRepository.findByUser(user);
    }
}
