package com.taskverse.service;

import com.taskverse.dto.request.ChangePasswordRequest;
import com.taskverse.dto.request.UserRegisterRequest;
import com.taskverse.dto.request.UserUpdateRequest;
import com.taskverse.dto.response.UserResponse;
import com.taskverse.entity.User;
import com.taskverse.exception.EmailAlreadyExistsException;
import com.taskverse.exception.InvalidPasswordException;
import com.taskverse.exception.UserNotFoundException;
import com.taskverse.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ==================== Public Methods ====================

    // Register User
    public UserResponse registerUser(UserRegisterRequest request) {

        String email = normalizeEmail(request.getEmail());

        validateEmailNotExists(email);

        String encodedPassword = encodePassword(request.getPassword());

        User user = buildUser(request, email, encodedPassword);

        User savedUser = saveUser(user);

        return mapToUserResponse(savedUser);
    }

    // Get User By ID
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {

        User user = findUserById(userId);

        return mapToUserResponse(user);
    }

    // Update User Profile
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {

        User user = findUserById(userId);

        updateUserDetails(user, request);

        User updatedUser = saveUser(user);

        return mapToUserResponse(updatedUser);
    }

    // Change Password
    public void changePassword(Long userId, ChangePasswordRequest request) {

        User user = findUserById(userId);

        validateCurrentPassword(user, request.getCurrentPassword());

        validateNewPassword(
                request.getCurrentPassword(),
                request.getNewPassword()
        );
        validatePasswordConfirmation(
                request.getNewPassword(),
                request.getConfirmNewPassword()
        );
        String encodedPassword = encodePassword(request.getNewPassword());

        user.changePassword(encodedPassword);

        saveUser(user);
    }

    // Delete User
    public void deleteUser(Long userId) {

        User user = findUserById(userId);

        userRepository.delete(user);
    }


    // ==================== Registration Methods ====================

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private void validateEmailNotExists(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private User buildUser(
            UserRegisterRequest request,
            String email,
            String encodedPassword
    ) {

        return new User(
                request.getFirstName(),
                request.getLastName(),
                email,
                encodedPassword,
                request.getPhone()
        );
    }


    // ==================== Update Methods ====================

    private void updateUserDetails(User user, UserUpdateRequest request) {

        user.updateProfile(
                request.getFirstName(),
                request.getLastName(),
                request.getPhone()
        );
    }


    // ==================== Password Methods ====================

    private void validateCurrentPassword(User user, String currentPassword) {

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }
    }

    private void validateNewPassword(
            String currentPassword,
            String newPassword
    ) {

        if (currentPassword.equals(newPassword)) {
            throw new InvalidPasswordException(
                    "New password must be different from current password"
            );
        }
    }
    private void validatePasswordConfirmation(
            String newPassword,
            String confirmNewPassword
    ) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new InvalidPasswordException(
                    "New password and confirmation password do not match"
            );
        }
    }


    // ==================== Common Methods ====================

    private User findUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private UserResponse mapToUserResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());

        return response;
    }
}