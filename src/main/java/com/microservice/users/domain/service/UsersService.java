package com.microservice.users.domain.service;

import com.microservice.users.domain.exception.BadRequestException;
import com.microservice.users.domain.exception.NotFoundException;
import com.microservice.users.domain.model.Users;
import com.microservice.users.domain.repository.UsersRepository;
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

    public Page<UserRes> getAll(Pageable pageable) {
        return usersRepository.findAllByDeletedAtIsNull(pageable).map(this::toUserRes);
    }

    public List<UserRes> getAllWithoutPagination() {
        return usersRepository.findAllUsers().stream().map(this::toUserRes).collect(Collectors.toList());
    }

    public Optional<UserRes> getById(Long id) {
        return usersRepository.findById(id).filter(u -> u.getDeletedAt() == null).map(this::toUserRes);
    }

    public Optional<UserRes> getByEmail(String email) {
        return usersRepository.findByEmail(email)
                .filter(u -> u.getDeletedAt() == null)
                .map(this::toUserRes);
    }

    public Optional<UserRes> getByProviderId(String providerId) {
        return usersRepository.findByProviderId(providerId)
                .filter(u -> u.getDeletedAt() == null)
                .map(this::toUserRes);
    }

    @Transactional
    public UserRes create(CreateUserReq dto) {
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
        return toUserRes(savedUser);
    }

    @Transactional
    public UserRes update(Long id, UpdateUserReq dto) {
        Users userToUpdate = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));

        userToUpdate.setName(dto.getName());
        userToUpdate.setEmail(dto.getEmail());
        userToUpdate.setAvatarUrl(dto.getAvatarUrl());
        userToUpdate.setUpdatedAt(Instant.now());

        Users updatedUser = usersRepository.save(userToUpdate);
        return toUserRes(updatedUser);
    }

    @Transactional
    public void softDelete(Long id) {
        usersRepository.softDeleteById(id);
    }

    @Transactional
    public void updatePassword(Long id, UpdateUserPasswordReq dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password does not match confirmation");
        }
        if (usersRepository.findById(id).filter(u -> u.getDeletedAt() == null).isEmpty()) {
            throw new NotFoundException("User with ID " + id + " not found");
        }

        String hashedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        usersRepository.updatePassword(id, hashedPassword);
    }
}
