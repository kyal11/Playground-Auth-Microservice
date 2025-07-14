package com.microservice.users.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.List;
@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable= false, unique = true)
    private String email;

    private String provider;

    private String providerId;

    private String avatarUrl;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceSession> deviceSessions;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OAuthToken> oAuthTokens;
}
