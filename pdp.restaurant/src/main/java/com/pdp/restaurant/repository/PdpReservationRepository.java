package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdpReservationRepository extends JpaRepository<PdpReservation, Long> {
    // JpaRepository đã có sẵn hàm findAll() để lấy toàn bộ danh sách
}