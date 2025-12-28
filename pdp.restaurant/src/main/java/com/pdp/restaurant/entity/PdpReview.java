package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pdp_review")
@Data
public class PdpReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment; // thêm trường này

    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private PdpUser user;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private PdpDish dish;

    // Setter cho user, dish, comment nếu không dùng Lombok
    public void setUser(PdpUser user) {
        this.user = user;
    }

    public void setDish(PdpDish dish) {
        this.dish = dish;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
