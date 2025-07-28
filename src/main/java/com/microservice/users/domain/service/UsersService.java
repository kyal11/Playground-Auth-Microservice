package com.microservice.users.domain.service;

import com.microservice.users.domain.exception.BadRequestException;
import com.microservice.users.domain.exception.NotFoundException;
import com.microservice.users.domain.model.Users;
import com.microservice.users.domain.repository.UsersRepository;
import com.microservice.users.dto.users.request.CreateUserRequest;
import com.microservice.users.dto.users.request.UpdateUserPassword;
import com.microservice.users.dto.users.request.UpdateUserRequest;
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
    public Users create(CreateUserRequest dto) {
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setProvider(dto.getProvider());
        user.setProviderId(dto.getProviderId());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return usersRepository.save(user);
    }

    @Transactional
    public Users update(Long id, UpdateUserRequest dto) {
        return usersRepository.findById(id)
                .map(user -> {
                    user.setName(dto.getName());
                    user.setEmail(dto.getEmail());
                    user.setAvatarUrl(dto.getAvatarUrl());
                    user.setUpdatedAt(Instant.now());
                    return usersRepository.save(user);
                })
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public void softDelete(Long id) {
        usersRepository.softDeleteById(id);
    }

    @Transactional
    public void updatePassword(Long id, UpdateUserPassword dto) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password does not match confirmation");
        }
        String hashedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        usersRepository.updatePassword(id, dto.getPassword());
    }
}
