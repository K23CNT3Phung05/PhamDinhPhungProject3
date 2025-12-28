package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carts")
@Data
public class PdpCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id") // liên kết với bảng users
    private PdpUser user;

    private int totalItems;
    private double totalPrice;

    // Constructor mặc định
    public PdpCart() {}

    // Constructor tiện lợi
    public PdpCart(PdpUser user) {
        this.user = user;
    }
}
