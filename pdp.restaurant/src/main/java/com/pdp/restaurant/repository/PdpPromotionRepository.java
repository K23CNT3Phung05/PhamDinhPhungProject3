package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdpPromotionRepository extends JpaRepository<PdpPromotion, Long> {
    PdpPromotion findByCodeAndActiveTrue(String code);
}
