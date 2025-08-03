package com.microservice.users.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRes {
    private Long id;
    private String name;
    private String email;
}
