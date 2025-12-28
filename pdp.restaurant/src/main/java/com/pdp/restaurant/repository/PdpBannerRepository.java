package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdpBannerRepository extends JpaRepository<PdpBanner, Long> {
    // Tất cả CRUD, findAll, findById, save, deleteById đều có sẵn
}
