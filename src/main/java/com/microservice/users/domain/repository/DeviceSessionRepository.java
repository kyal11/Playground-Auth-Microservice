package com.microservice.users.domain.repository;

import com.microservice.users.domain.model.DeviceSession;
import com.microservice.users.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceSessionRepository extends JpaRepository<DeviceSession, Long> {

    Optional<DeviceSession> findById(Long id);

    Optional<DeviceSession> findByUserId(Long userId);

    List<DeviceSession> findAll();

    List<DeviceSession> findAllByUser(Users user);

    DeviceSession save(DeviceSession device);

    void deleteAllByUserId(Long userId);
}
