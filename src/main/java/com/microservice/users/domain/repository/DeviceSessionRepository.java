package com.microservice.users.domain.repository;

import com.microservice.users.domain.model.DeviceSession;
import com.microservice.users.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceSessionRepository extends JpaRepository<DeviceSession, Long> {

    Optional<DeviceSession> findById(Long id);

    Optional<DeviceSession> findByDeviceId(String deviceId);

    Optional<DeviceSession> findByUserId(Long userId);

    List<DeviceSession> findAll();

    List<DeviceSession> findAllByDeviceId(String deviceId);

    List<DeviceSession> findAllByUser(Users user);

    DeviceSession save(DeviceSession device);
}
