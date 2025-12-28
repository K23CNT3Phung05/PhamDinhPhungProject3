package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pdp_banner")
@Data
public class PdpBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String imageUrl;
    private Boolean active;
}
