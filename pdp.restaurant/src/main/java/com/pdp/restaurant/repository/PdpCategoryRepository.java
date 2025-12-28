package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpCategory; // 1. BẮT BUỘC phải có dòng này
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional; // Import để dùng Optional

@Repository
public interface PdpCategoryRepository extends JpaRepository<PdpCategory, Long> {

    /**
     * Đếm tổng số danh mục.
     * Thực tế JpaRepository đã có sẵn hàm .count(),
     * bạn có thể dùng trực tiếp mà không cần viết @Query này.
     */
    @Query("SELECT COUNT(c) FROM PdpCategory c")
    long getCategoryCount();

    /**
     * Tìm danh mục theo tên.
     * Sử dụng Optional để tránh lỗi NullPointerException nếu không tìm thấy.
     */
    Optional<PdpCategory> findByName(String name);

    // Kiểm tra xem tên danh mục đã tồn tại chưa (thường dùng khi thêm mới)
    boolean existsByName(String name);
}