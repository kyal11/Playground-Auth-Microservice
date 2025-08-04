package com.microservice.users.controller;

import com.microservice.users.domain.service.UsersService;
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
    public ResponseEntity<Page<UserRes>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(usersService.getAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserRes>> getAllWithoutPagination() {
        return ResponseEntity.ok(usersService.getAllWithoutPagination());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRes> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<UserRes> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(usersService.getByEmail(email));
    }

    @GetMapping("/provider")
    public ResponseEntity<UserRes> getUserByProviderId(@RequestParam String providerId) {
        return ResponseEntity.ok(usersService.getByProviderId(providerId));
    }

    @PostMapping
    public ResponseEntity<UserRes> createUser(@RequestParam CreateUserReq request) {
        UserRes newUser = usersService.create(request);
        return ResponseEntity.created(URI.create("/api/users/" + newUser.getId())).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRes> update(@PathVariable Long id, @RequestBody UpdateUserReq request) {
        UserRes updatedUser = usersService.update(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.softDelete(id));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody UpdateUserPasswordReq request) {
        return ResponseEntity.ok(usersService.updatedPassword(id, request));
    }
}
