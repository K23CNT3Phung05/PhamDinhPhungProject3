package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.entity.PdpWalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PdpWalletTransactionRepository extends JpaRepository<PdpWalletTransaction, Long> {

    // Lấy các giao dịch của user, sắp xếp theo createdAt giảm dần
    List<PdpWalletTransaction> findByUserOrderByCreatedAtDesc(PdpUser user);

}
