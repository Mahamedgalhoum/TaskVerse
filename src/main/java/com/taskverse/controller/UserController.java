package com.taskverse.controller;

import com.taskverse.dto.request.ChangePasswordRequest;
import com.taskverse.dto.request.UserUpdateRequest;
import com.taskverse.dto.response.ApiResponse;
import com.taskverse.dto.response.UserResponse;
import com.taskverse.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
        UserResponse currentUser = userService.getCurrentUser(authentication.getName());
        ApiResponse<UserResponse> response =
                ApiResponse.success("User Retrieved Successfully", currentUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser
            (Authentication authentication,
             @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse updatedUser = userService.updateCurrentUser(authentication.getName(), userUpdateRequest);

        ApiResponse<UserResponse> response = ApiResponse.success("User Updated Successfully", updatedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changeCurrentUserPassword
            (Authentication authentication, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {

        userService.changeCurrentUserPassword(authentication.getName(), changePasswordRequest);

        ApiResponse<Void> response = ApiResponse.success("Password Changed Successfully", null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> disableCurrentUser(Authentication authentication) {
        userService.disableCurrentUser(authentication.getName());
        ApiResponse<Void> response = ApiResponse.success("User Disabled Successfully", null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }



}
