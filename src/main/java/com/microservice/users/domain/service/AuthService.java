package com.microservice.users.domain.service;

import com.microservice.users.config.JwtConfig;
import com.microservice.users.domain.exception.BadRequestException;
import com.microservice.users.domain.model.RevokedToken;
import com.microservice.users.domain.model.Users;
import com.microservice.users.domain.repository.RevokedTokenRepository;
import com.microservice.users.domain.repository.UsersRepository;
import com.microservice.users.dto.auth.request.LoginReq;
import com.microservice.users.dto.auth.request.RegisterReq;
import com.microservice.users.dto.auth.response.LoginRes;
import com.microservice.users.dto.auth.response.RegisterRes;
import com.microservice.users.dto.deviceSession.request.RegisterNewSessionReq;
import com.microservice.users.dto.users.request.CreateUserReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepository usersRepository;
    private final DeviceSessionService deviceSessionService;
    private final RevokedTokenRepository revokedTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    @Transactional
    public RegisterRes register(RegisterReq dto) {
        if (usersRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setProvider("local");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        Users savedUsers = usersRepository.save(user);
        return new RegisterRes(savedUsers.getId(), savedUsers.getName(), savedUsers.getEmail());
    }

    @Transactional
    public LoginRes login(LoginReq dto, String deviceInfo) {
        Users user = usersRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Email not found"));

        if (user.getDeletedAt() != null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Credential!");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        RegisterNewSessionReq newSessionReq = new RegisterNewSessionReq();
        newSessionReq.setUser(user);
        newSessionReq.setDeviceId(deviceInfo);
        newSessionReq.setJwtToken(token);

        deviceSessionService.registerNewSession(newSessionReq, jwtService.extractExpired());

        return new LoginRes(user.getName(), user.getEmail(), token);
    }

    @Transactional
    public String logout(String token) {
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedToken.setRevokedAt(Instant.now());
        revokedTokenRepository.save(revokedToken);

        Long userId = jwtService.extractUserId(token);
        deviceSessionService.deleteAllByUserId(userId);

        return "Logout successful";
    }
}
