package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PdpFeedbackRepository extends JpaRepository<PdpFeedback, Long> {

    @Query("SELECT AVG(f.rating) FROM PdpFeedback f")
    Double getAverageRating();
}
