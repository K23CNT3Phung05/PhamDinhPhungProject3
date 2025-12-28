package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pdp_order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdpOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PHẢI CÓ DÒNG NÀY ĐỂ HẾT LỖI Ở SERVICE
    @Column(name = "dish_name")
    private String dishName;

    private BigDecimal price;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private PdpDish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private PdpOrder order;
}