package com.microservice.users.domain.service;

import com.microservice.users.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private SecretKey secretKey;
    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getJwtSecret().getBytes());
    }

    public String generateToken(Long userId, String email) {
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("email", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtConfig.getJwtExpiration())))
                .signWith(secretKey, SignatureAlgorithm.ES256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public Instant extractExpired() {
        return  Instant.now().plusMillis(jwtConfig.getJwtExpiration());
    }
    public Long extractUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }
}
