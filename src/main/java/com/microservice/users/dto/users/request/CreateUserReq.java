package com.microservice.users.dto.users.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserReq {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;

    @NotBlank
    @Min(8)
    private String password;

    private String provider;
    private String providerId;
    private String avatarUrl;
}
