package com.microservice.users.domain.service;

import com.microservice.users.domain.exception.BadRequestException;
import com.microservice.users.domain.exception.NotFoundException;
import com.microservice.users.domain.model.Users;
import com.microservice.users.domain.repository.UsersRepository;
import com.microservice.users.dto.ApiResponse;
import com.microservice.users.dto.users.request.CreateUserReq;
import com.microservice.users.dto.users.request.UpdateUserPasswordReq;
import com.microservice.users.dto.users.request.UpdateUserReq;
import com.microservice.users.dto.users.response.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRes toUserRes(Users user) {
        UserRes res = new UserRes();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAvatarUrl(user.getAvatarUrl());
        res.setProvider(user.getProvider());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public ApiResponse<Page<UserRes>> getAll(Pageable pageable) {
        Page<UserRes> users = usersRepository.findAllByDeletedAtIsNull(pageable).map(this::toUserRes);
        return ApiResponse.success("Get all users successfully", users);
    }

    public ApiResponse<List<UserRes>> getAllWithoutPagination() {
        List<UserRes> users = usersRepository.findAllUsers().stream().map(this::toUserRes).collect(Collectors.toList());
        return ApiResponse.success("Get all users successfully", users);
    }

    public ApiResponse<UserRes> getById(Long id) {
        Users user = usersRepository.findById(id)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        return ApiResponse.success("Get user by ID successfully", toUserRes(user));
    }

    public ApiResponse<UserRes> getByEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
        return ApiResponse.success("Get user by email successfully", toUserRes(user));
    }

    public ApiResponse<UserRes> getByProviderId(String providerId) {
        Users user = usersRepository.findByProviderId(providerId)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with provider ID " + providerId + " not found"));
        return ApiResponse.success("Get user by provider ID successfully", toUserRes(user));
    }

    @Transactional
    public ApiResponse<UserRes> create(CreateUserReq dto) {
        if (usersRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setProvider(dto.getProvider());
        user.setProviderId(dto.getProviderId());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        Users savedUser = usersRepository.save(user);
        return ApiResponse.success("Create user successfully", toUserRes(savedUser));
    }

    @Transactional
    public ApiResponse<UserRes> update(Long id, UpdateUserReq dto) {
        Users userToUpdate = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));

        userToUpdate.setName(dto.getName());
        userToUpdate.setEmail(dto.getEmail());
        userToUpdate.setAvatarUrl(dto.getAvatarUrl());
        userToUpdate.setUpdatedAt(Instant.now());

        Users updatedUser = usersRepository.save(userToUpdate);
        return ApiResponse.success("Update user successfully", toUserRes(updatedUser));
    }

    @Transactional
    public ApiResponse<String> softDelete(Long id) {
        Users user = usersRepository.findById(id)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        usersRepository.softDeleteById(id);

        return ApiResponse.success("Delete user successfully!");
    }

    @Transactional
    public ApiResponse<String> updatedPassword(Long id, UpdateUserPasswordReq dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password does not match confirmation");
        }
        if (usersRepository.findById(id).filter(u -> u.getDeletedAt() == null).isEmpty()) {
            throw new NotFoundException("User with ID " + id + " not found");
        }

        String hashedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        usersRepository.updatePassword(id, hashedPassword);

        return ApiResponse.success("Update password successfully!");
    }
}
