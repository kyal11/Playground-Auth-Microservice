package com.microservice.users.domain.service;

import com.microservice.users.domain.model.DeviceSession;
import com.microservice.users.domain.model.RevokedToken;
import com.microservice.users.domain.model.Users;
import com.microservice.users.domain.repository.DeviceSessionRepository;
import com.microservice.users.domain.repository.RevokedTokenRepository;
import com.microservice.users.dto.deviceSession.request.RegisterNewSessionReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceSessionService {

    private final DeviceSessionRepository deviceSessionRepository;
    private final RevokedTokenRepository revokedTokenRepository;


    @Transactional
    public void registerNewSession(RegisterNewSessionReq dto, Instant expiredAt) {
        Optional<DeviceSession> existingSessionUser = deviceSessionRepository.findByUserId(dto.getUser().getId());

        existingSessionUser.ifPresent(deviceSession ->  {
            RevokedToken revokedToken = new RevokedToken();
            revokedToken.setToken(deviceSession.getJwtToken());
            revokedToken.setRevokedAt(Instant.now());
            revokedTokenRepository.save(revokedToken);

            deviceSessionRepository.delete(deviceSession);
        });

        DeviceSession newSession = new DeviceSession();
        newSession.setUser(dto.getUser());
        newSession.setDeviceId(dto.getDeviceId());
        newSession.setJwtToken(dto.getJwtToken());
        newSession.setCreatedAt(Instant.now());
        newSession.setExpiredAt(expiredAt);
        deviceSessionRepository.save(newSession);
    }

    public Optional<DeviceSession> getActiveSessionByUser(Long userId) {
        return deviceSessionRepository.findByUserId(userId);
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokenRepository.existsByToken(token);
    }
    @Transactional
    public void deleteAllByUserId(long userId) {
        deviceSessionRepository.deleteAllByUserId(userId);
    }
}
