package com.taskverse.controller;

import com.taskverse.dto.request.LoginRequest;
import com.taskverse.dto.request.UserRegisterRequest;
import com.taskverse.dto.response.ApiResponse;
import com.taskverse.dto.response.LoginResponse;
import com.taskverse.dto.response.UserResponse;
import com.taskverse.service.AuthService;
import com.taskverse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(
            @Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        UserResponse userResponse = userService.registerUser(userRegisterRequest);
        ApiResponse<UserResponse> response =
                ApiResponse.success("User registered successfully", userResponse);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        String accessToken = authService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        LoginResponse loginResponse = new LoginResponse(accessToken , "Bearer" ,authService.getExpirationInSeconds());

        ApiResponse<LoginResponse> response =
                ApiResponse.success("Login successful", loginResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
