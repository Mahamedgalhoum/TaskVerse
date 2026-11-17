package com.taskverse.service;

import com.taskverse.dto.request.*;
import com.taskverse.dto.response.AdminUserResponse;
import com.taskverse.dto.response.UserResponse;
import com.taskverse.entity.User;
import com.taskverse.enums.Role;
import com.taskverse.exception.EmailAlreadyExistsException;
import com.taskverse.exception.InvalidPasswordException;
import com.taskverse.exception.UserNotFoundException;
import com.taskverse.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountDeletionSettingService accountDeletionSettingService;

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


    // Get Current User
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String email) {

        User user = findUserByEmail(email);

        return mapToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public AdminUserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        return mapToAdminUserResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<AdminUserResponse> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(this::mapToAdminUserResponse);
    }



    // Update Current User Profile
    public UserResponse updateCurrentUser(
            String email,
            UserUpdateRequest request) {

        User user = findUserByEmail(email);

        updateUserDetails(user, request);

        User updatedUser = saveUser(user);

        return mapToUserResponse(updatedUser);
    }


    public void changeCurrentUserPassword(String email, ChangePasswordRequest request) {

        User user = findUserByEmail(email);

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

        user.incrementTokenVersion();

        saveUser(user);
    }

    public AdminUserResponse changeUserRole(
            Long id,
            ChangeUserRoleRequest request,
            String currentUserEmail) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // Prevent changing your own role
        if (user.getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new IllegalArgumentException(
                    "You cannot change your own role");
        }

        Role newRole = request.getRole();

        // Check if the role is actually changing
        if (user.getRole() == newRole) {
            return mapToAdminUserResponse(user);
        }

        validateLastAdminRoleChange(user, newRole);

        user.changeRole(newRole);
        user.incrementTokenVersion();

        saveUser(user);

        return mapToAdminUserResponse(user);
    }

    public AdminUserResponse changeUserStatus(
            Long id,
            ChangeUserStatusRequest request,
            String currentUserEmail) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        validateNotCurrentUser(user, currentUserEmail);

        boolean newStatus = request.getEnabled();

        if (user.isEnabled() == newStatus) {
            return mapToAdminUserResponse(user);
        }

        validateLastAdmin(user, newStatus);

        updateAccountStatus(user, newStatus);

        user.incrementTokenVersion();

        saveUser(user);

        return mapToAdminUserResponse(user);
    }

    // Soft Delete Current User
    public void disableCurrentUser(String email) {

        User user = findUserByEmail(email);
        validateLastAdmin(user, false);
        LocalDateTime deletionDate = LocalDateTime.now()
                .plusDays(accountDeletionSettingService.getGracePeriodDays());

        user.scheduleDeletion(deletionDate);
        user.incrementTokenVersion();

        saveUser(user);
    }
    public void reactivateAccount(AccountReactivationRequest request) {

        String email = normalizeEmail(request.getEmail());

        User user = findUserByEmail(email);

        validateReactivationCredentials(user, request.getPassword());

        validateAccountEligibleForReactivation(user);

        user.reactivateAccount();
        user.incrementTokenVersion();

        saveUser(user);
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

    // ==================== Account Status Methods ====================
    private void validateNotCurrentUser(User user, String currentUserEmail) {

        if (user.getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new IllegalArgumentException(
                    "You cannot change your own account status");
        }
    }
    private void validateLastAdmin(User user, boolean newStatus) {

        if (user.getRole() == Role.ADMIN
                && user.isEnabled()
                && !newStatus
                && userRepository.countByRoleAndEnabledTrue(Role.ADMIN) <= 1) {

            throw new IllegalStateException(
                    "Cannot disable the last admin"
            );
        }
    }
    private void validateLastAdminRoleChange(User user, Role newRole) {

        if (user.getRole() == Role.ADMIN
                && user.isEnabled()
                && newRole != Role.ADMIN
                && userRepository.countByRoleAndEnabledTrue(Role.ADMIN) <= 1) {

            throw new IllegalStateException(
                    "Cannot remove the last admin"
            );
        }
    }
    private void updateAccountStatus(User user, boolean enabled) {

        if (enabled) {
            user.reactivateAccount();
        } else {
            user.disableAccount();
        }
    }

    private void validateReactivationCredentials(User user, String rawPassword) {

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidPasswordException("Invalid email or password");
        }
    }
    private void validateAccountEligibleForReactivation(User user) {

        if (user.isEnabled() || user.getDeletionScheduledAt() == null) {
            throw new IllegalStateException(
                    "Account is not eligible for reactivation"
            );
        }

        if (!user.getDeletionScheduledAt().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException(
                    "Account reactivation period has expired"
            );
        }
    }

    // ==================== Common Methods ====================

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private User findUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
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
    private AdminUserResponse mapToAdminUserResponse(User user) {

        AdminUserResponse response = new AdminUserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setEnabled(user.isEnabled());

        return response;
    }
}