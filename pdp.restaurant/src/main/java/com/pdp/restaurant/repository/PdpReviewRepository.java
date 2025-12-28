package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpDish;
import com.pdp.restaurant.entity.PdpReview;
import com.pdp.restaurant.entity.PdpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdpReviewRepository extends JpaRepository<PdpReview, Long> {

    List<PdpReview> findByDish(PdpDish dish);

    List<PdpReview> findByUser(PdpUser user);

    // Điểm đánh giá trung bình
    @Query("SELECT AVG(r.rating) FROM PdpReview r")
    Double getAverageRating();
}
