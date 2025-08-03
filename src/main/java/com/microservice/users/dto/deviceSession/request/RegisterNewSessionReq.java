package com.microservice.users.dto.deviceSession.request;

import com.microservice.users.domain.model.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterNewSessionReq {
    @NotBlank
    private Users user;

    @NotBlank
    private String deviceId;

    @NotBlank
    private String jwtToken;
}
