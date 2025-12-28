package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.entity.PdpWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PdpWalletRepository extends JpaRepository<PdpWallet, Long> {
    Optional<PdpWallet> findByUser(PdpUser user);
}
