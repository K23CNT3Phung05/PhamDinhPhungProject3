package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pdp_category")
@Getter // Khuyên dùng Getter/Setter thay vì @Data cho Entity để tránh lỗi hiệu năng
@Setter
@NoArgsConstructor // Thay thế cho public PdpCategory() {}
@AllArgsConstructor // Tạo constructor cho tất cả các trường
@ToString(exclude = "dishes") // Loại trừ dishes để tránh lỗi vòng lặp vô tận
@EqualsAndHashCode(exclude = "dishes")
public class PdpCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Quan hệ 1-N với Dish
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PdpDish> dishes = new ArrayList<>();

    // Constructor tùy chỉnh nếu bạn chỉ muốn tạo Category với tên
    public PdpCategory(String name) {
        this.name = name;
    }

    // --- Helper Methods để thêm/xóa Dish thuận tiện hơn ---
    public void addDish(PdpDish dish) {
        dishes.add(dish);
        dish.setCategory(this);
    }

    public void removeDish(PdpDish dish) {
        dishes.remove(dish);
        dish.setCategory(null);
    }
}