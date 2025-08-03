package com.microservice.users.domain.repository;

import com.microservice.users.domain.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

    Optional<RevokedToken> findById(Long aLong);

    Optional<RevokedToken> findByToken(String token);

    void deleteById(Long id);

    void deleteByToken(String token);

    RevokedToken Save(RevokedToken token);

    boolean existByToken(String token);
}
