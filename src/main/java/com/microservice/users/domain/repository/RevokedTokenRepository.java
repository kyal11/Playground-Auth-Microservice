package com.microservice.users.domain.repository;

import com.microservice.users.domain.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

    Optional<RevokedToken> findById(Long aLong);

    Optional<RevokedToken> findByToken(String token);

    void deleteById(Long id);

    void deleteByToken(String token);

    RevokedToken save(RevokedToken token);

    boolean existsByToken(String token);
}
