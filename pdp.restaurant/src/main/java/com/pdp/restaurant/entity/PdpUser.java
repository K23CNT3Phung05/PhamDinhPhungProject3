package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "pdp_user",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_full_name", columnList = "full_name")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdpUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên đăng nhập (dùng để login song song với email)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    // Email đăng nhập
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Mật khẩu (đã encode)
    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Role role = Role.USER;

    public enum Role {
        USER, STAFF, ADMIN
    }

    // ================== RELATION ==================

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PdpCart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PdpOrder> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PdpFeedback> feedbacks = new ArrayList<>();
}
