package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "pdp_feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdpFeedback implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ====== USER ======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private PdpUser user;

    // ====== ORDER (optional) ======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private PdpOrder order;

    // ====== DISH (optional) ======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private PdpDish dish;

    @Column(nullable = false)
    private String content;

    @Column
    private Integer rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
