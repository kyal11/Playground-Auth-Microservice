package com.microservice.users.dto.users.response;

import lombok.Data;

import java.time.Instant;

@Data
public class UserRes {
    private Long id;
    private String name;
    private String email;
    private String avatarUrl;
    private String provider;
    private Instant createdAt;
    private Instant updatedAt;
}
