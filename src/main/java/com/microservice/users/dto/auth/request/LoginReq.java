package com.microservice.users.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Min(8)
    private String password;

    private String deviceInfo;
}
