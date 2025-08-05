package com.microservice.users.controller;

import com.microservice.users.domain.service.UsersService;
import com.microservice.users.dto.ApiResponse;
import com.microservice.users.dto.users.request.CreateUserReq;
import com.microservice.users.dto.users.request.UpdateUserPasswordReq;
import com.microservice.users.dto.users.request.UpdateUserReq;
import com.microservice.users.dto.users.response.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserRes>>> getAllUsers(Pageable pageable) {
        ApiResponse<Page<UserRes>> response = usersService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserRes>>> getAllWithoutPagination() {
        ApiResponse<List<UserRes>> response = usersService.getAllWithoutPagination();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRes>> getUserById(@PathVariable Long id) {
        ApiResponse<UserRes> response = usersService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserRes>> getUserByEmail(@RequestParam String email) {
        ApiResponse<UserRes> response = usersService.getByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/provider")
    public ResponseEntity<ApiResponse<UserRes>> getUserByProviderId(@RequestParam String providerId) {
        ApiResponse<UserRes> response = usersService.getByProviderId(providerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserRes>> createUser(@RequestBody CreateUserReq request) {
        ApiResponse<UserRes> response = usersService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/users/" + response.getData().getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRes>> update(@PathVariable Long id, @RequestBody UpdateUserReq request) {
        ApiResponse<UserRes> response = usersService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> softDelete(@PathVariable Long id) {
        ApiResponse<String> response = usersService.softDelete(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(@PathVariable Long id, @RequestBody UpdateUserPasswordReq request) {
        ApiResponse<String> response = usersService.updatedPassword(id, request);
        return ResponseEntity.ok(response);
    }
}
