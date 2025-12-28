package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PdpUserRepository extends JpaRepository<PdpUser, Long> {

    Optional<PdpUser> findByEmail(String email);

    Optional<PdpUser> findByFullName(String fullName);

    boolean existsByEmail(String email);

    boolean existsByFullName(String fullName);



}
