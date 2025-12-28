package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pdp_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdpOrder {
    private String fullName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private PdpUser user;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;

    @Column(name = "customer_address", nullable = false)
    private String customerAddress;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "shipping_fee", precision = 15, scale = 2)
    private BigDecimal shippingFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private PdpOrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PdpPaymentMethod paymentMethod;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- CÁC TRƯỜNG MỚI CHO PHẦN REVIEW ---
    @Column(name = "rating")
    private Integer rating;

    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String reviewComment;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
    // ---------------------------------------

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<PdpOrderItem> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.orderStatus == null) {
            this.orderStatus = PdpOrderStatus.PENDING;
        }
    }
}