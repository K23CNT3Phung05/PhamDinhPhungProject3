package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpCategory;
import com.pdp.restaurant.entity.PdpDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PdpDishRepository extends JpaRepository<PdpDish, Long> {

    List<PdpDish> findByNameContainingIgnoreCase(String name);

    List<PdpDish> findByCategory(PdpCategory category);

    // SỬA TẠI ĐÂY: Thêm IgnoreCase để tránh lỗi chữ hoa/thường
    List<PdpDish> findByCategory_NameIgnoreCase(String categoryName);

    List<PdpDish> findByActiveTrue();

    List<PdpDish> findByCategory_NameContainingIgnoreCase(String trim);
}