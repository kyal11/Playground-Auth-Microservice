package com.microservice.users.domain.service;

import com.microservice.users.domain.model.Users;
import com.microservice.users.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Page<Users> getAll(Pageable pageable) {
        return usersRepository.findAllByDeletedAtIsNull(pageable);
    }

    public List<Users> getAllWithoutPagination() {
        return usersRepository.findAllUsers();
    }

    public Optional<Users> getById(Long id) {
        return usersRepository.findById(id).filter(u -> u.getDeletedAt() == null);
    }

    public Optional<Users> getByEmail(String email) {
        return usersRepository.findByEmail(email).filter(u -> u.getDeletedAt() == null);
    }

    public Optional<Users> getByProviderId(String providerId) {
        return usersRepository.findByProviderId(providerId).filter(u -> u.getDeletedAt() == null);
    }

    @Transactional
    public Users create(Users user) {
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

    @Transactional
    public Users update(Long id, Users updatedUser) {
        return usersRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setAvatarUrl(updatedUser.getAvatarUrl());
                    user.setUpdatedAt(Instant.now());
                    return usersRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void softDelete(Long id) {
        usersRepository.softDeleteById(id);
    }

    @Transactional
    public void updatePassword(Long id, String password, String confirmPassword) {
        if(!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password not match!");
        }
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        usersRepository.updatePassword(id, password);
    }
}
