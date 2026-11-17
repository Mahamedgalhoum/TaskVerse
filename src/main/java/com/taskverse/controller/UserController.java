package com.taskverse.controller;

import com.taskverse.dto.request.ChangePasswordRequest;
import com.taskverse.dto.request.ChangeUserRoleRequest;
import com.taskverse.dto.request.ChangeUserStatusRequest;
import com.taskverse.dto.request.UserUpdateRequest;
import com.taskverse.dto.response.AdminUserResponse;
import com.taskverse.dto.response.ApiResponse;
import com.taskverse.dto.response.UserResponse;
import com.taskverse.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AdminUserResponse>>> getAllUsers(Pageable pageable) {

        Page<AdminUserResponse> users = userService.getAllUsers(pageable);
        ApiResponse<Page<AdminUserResponse>> response = ApiResponse.success("Users Retrieved Successfully", users);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);


    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminUserResponse>> getUserById(
            @PathVariable Long id) {

        AdminUserResponse user = userService.getUserById(id);

        ApiResponse<AdminUserResponse> response =
                ApiResponse.success("User Retrieved Successfully", user);

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminUserResponse>> changeUserRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserRoleRequest request,
            Authentication authentication) {

        AdminUserResponse user = userService.changeUserRole(
                id,
                request,
                authentication.getName()
        );

        ApiResponse<AdminUserResponse> response =
                ApiResponse.success("User Role Updated Successfully", user);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminUserResponse>> changeUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserStatusRequest request,
            Authentication authentication) {

        AdminUserResponse user = userService.changeUserStatus(
                id,
                request,
                authentication.getName()
        );

        ApiResponse<AdminUserResponse> response =
                ApiResponse.success("User Status Updated Successfully", user);

        return ResponseEntity.ok(response);
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
