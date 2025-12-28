package com.pdp.restaurant.service;

import com.pdp.restaurant.entity.PdpCategory;
import com.pdp.restaurant.entity.PdpDish;
import com.pdp.restaurant.repository.PdpCategoryRepository;
import com.pdp.restaurant.repository.PdpDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PdpDishService {

    private final PdpDishRepository dishRepository;
    private final PdpCategoryRepository categoryRepository;

    public List<PdpDish> getAll() {
        return dishRepository.findAll();
    }

    public PdpDish getById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn với ID: " + id));
    }

    @Transactional
    public PdpDish save(PdpDish dish) {
        if (dish.getCategory() != null && dish.getCategory().getId() != null) {
            PdpCategory category = categoryRepository.findById(dish.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục ID: " + dish.getCategory().getId()));
            dish.setCategory(category);
        }

        if (dish.getPrice() == null || dish.getPrice().doubleValue() < 0) {
            throw new RuntimeException("Giá món ăn không hợp lệ!");
        }

        if (dish.getStockQuantity() == null || dish.getStockQuantity() < 0) {
            dish.setStockQuantity(0);
        }

        if (dish.getId() == null) {
            dish.setActive(true);
        }

        return dishRepository.save(dish);
    }

    @Transactional
    public void delete(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new RuntimeException("Không thể xóa! Món ăn không tồn tại ID: " + id);
        }
        dishRepository.deleteById(id);
    }

    public List<PdpDish> getByCategory(Long categoryId) {
        PdpCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại ID: " + categoryId));
        return dishRepository.findByCategory(category);
    }

    // --- PHẦN SỬA LỖI TẠI ĐÂY ---
    public List<PdpDish> getByCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return dishRepository.findAll();
        }

        return dishRepository.findByCategory_NameContainingIgnoreCase(categoryName.trim());
    }

    public List<PdpDish> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return dishRepository.findAll();
        }
        return dishRepository.findByNameContainingIgnoreCase(keyword.trim());
    }

    public List<PdpDish> getFeaturedDishes() {
        return dishRepository.findByActiveTrue();
    }

    public PdpDish findById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }
}