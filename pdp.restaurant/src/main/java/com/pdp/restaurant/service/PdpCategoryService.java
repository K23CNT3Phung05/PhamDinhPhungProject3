package com.pdp.restaurant.service;

import com.pdp.restaurant.entity.PdpCategory;
import com.pdp.restaurant.repository.PdpCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PdpCategoryService {

    private final PdpCategoryRepository categoryRepository;

    // Lấy tất cả danh mục
    public List<PdpCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Tìm theo ID (Trả về Optional để dùng trong các logic kiểm tra)
    public Optional<PdpCategory> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Tìm theo ID và trả về Object (SỬA CHUẨN: Dùng cho Controller)
    public PdpCategory getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));
    }

    // Lưu hoặc cập nhật danh mục
    @Transactional
    public PdpCategory saveCategory(PdpCategory category) {
        return categoryRepository.save(category);
    }

    // Xóa danh mục
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Đếm tổng số danh mục (Dùng cho Dashboard)
    public long countCategories() {
        return categoryRepository.count();
    }
}