package com.microservice.users.dto.auth.request;

import lombok.Data;

@Data
public class LogoutReq {
    private String token;
}
