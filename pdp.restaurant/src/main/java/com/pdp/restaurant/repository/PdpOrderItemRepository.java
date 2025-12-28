package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdpOrderItemRepository extends JpaRepository<PdpOrderItem, Long> {
    // Không cần định nghĩa save() nữa, đã được kế thừa từ JpaRepository
}
