package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// ================== PdpWallet ==================
@Entity
@Table(name = "pdp_wallet")
public class PdpWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private PdpUser user;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    // Constructors
    public PdpWallet() {
    }

    public PdpWallet(PdpUser user) {
        this.user = user;
        this.balance = BigDecimal.ZERO;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PdpUser getUser() {
        return user;
    }

    public void setUser(PdpUser user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}