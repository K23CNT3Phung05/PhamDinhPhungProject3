package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdpCartRepository extends JpaRepository<PdpCart, Long> {
    // Các method tuỳ chỉnh nếu cần
}
