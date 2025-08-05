package com.microservice.users.controller;

import com.microservice.users.domain.service.AuthService;
import com.microservice.users.dto.ApiResponse;
import com.microservice.users.dto.auth.request.LoginReq;
import com.microservice.users.dto.auth.request.LogoutReq;
import com.microservice.users.dto.auth.request.RegisterReq;
import com.microservice.users.dto.auth.response.LoginRes;
import com.microservice.users.dto.auth.response.RegisterRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterRes>> register(@RequestBody RegisterReq request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRes>> login(@RequestBody LoginReq request) {
        return ResponseEntity.ok(authService.login(request, request.getDeviceInfo()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody LogoutReq request) {
        return ResponseEntity.ok(authService.logout(request.getToken()));
    }

}
