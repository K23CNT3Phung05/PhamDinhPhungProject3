package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pdp_wallet_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdpWalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= USER =================
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private PdpUser user;

    // ================= SỐ TIỀN =================
    @Column(nullable = false)
    private BigDecimal amount;

    // ================= LOẠI GIAO DỊCH =================
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private PdpTransactionType transactionType;

    // ================= PHƯƠNG THỨC =================
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PdpPaymentMethod paymentMethod;

    // ================= MÔ TẢ =================
    private String description;

    // ================= THỜI GIAN =================
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
