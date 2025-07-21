package com.microservice.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public Long getJwtExpiration() {
        return jwtExpiration;
    }
}
