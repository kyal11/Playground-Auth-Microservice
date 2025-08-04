package com.microservice.users.domain.repository;

import com.microservice.users.domain.model.OAuthToken;
import com.microservice.users.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OAuthRepository extends JpaRepository<OAuthToken, Long> {

    Optional<OAuthToken> findByAccessToken(String accessToken);

    Optional<OAuthToken> findByRefreshToken(String refreshToken);

    List<OAuthToken> findAllByUser(Users user);

    OAuthToken save(OAuthToken oAuthToken);

    void deleteAllByUser(Users user);

    void deleteById(Long id);
}
