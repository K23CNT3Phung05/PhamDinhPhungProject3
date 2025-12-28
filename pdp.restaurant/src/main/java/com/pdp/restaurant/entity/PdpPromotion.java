package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "pdp_promotion")
@Data
public class PdpPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private int discountPercentage;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Boolean active = true;
}
