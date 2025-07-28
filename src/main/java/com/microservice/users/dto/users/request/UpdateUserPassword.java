package com.microservice.users.dto.users.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserPassword {
    @NotBlank
    @Min(8)
    private String password;

    @NotBlank
    @Min(8)
    private String confirmPassword;
}
