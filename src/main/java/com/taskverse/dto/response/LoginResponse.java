package com.taskverse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter

public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
}
