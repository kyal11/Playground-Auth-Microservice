package com.microservice.users.dto.users.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserReq {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String avatarUrl;
}

