
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // ==================== Registration Tests ====================

    @Test
    void shouldRegisterUserSuccessfully() {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();

        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("Mohamed@Gmail.com");
        request.setPassword("TaskVerse@123");
        request.setPhone("01026368065");

        when(userRepository.existsByEmail("mohamed@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("TaskVerse@123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponse response = userService.registerUser(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Mohamed");
        assertThat(response.getLastName()).isEqualTo("Galhoum");
        assertThat(response.getEmail()).isEqualTo("mohamed@gmail.com");
        assertThat(response.getPhone()).isEqualTo("01026368065");

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).existsByEmail("mohamed@gmail.com");
        verify(passwordEncoder).encode("TaskVerse@123");
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();

        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("TaskVerse@123");
        request.setPhone("01026368065");

        when(userRepository.existsByEmail("mohamed@gmail.com"))
                .thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    // ==================== Get Current User Tests ====================

    @Test
    void shouldGetCurrentUserSuccessfully() {

        // Arrange
        String email = "mohamed@gmail.com";

        User user = new User(
                "Mohamed",
                "Galhoum",
                email,
                "encodedPassword",
                "01026368065"
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        // Act
        UserResponse response = userService.getCurrentUser(email);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Mohamed");
        assertThat(response.getLastName()).isEqualTo("Galhoum");
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getPhone()).isEqualTo("01026368065");

        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenCurrentUserDoesNotExist() {

        // Arrange
        String email = "notfound@gmail.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getCurrentUser(email))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByEmail(email);
    }

    // ==================== Update Current User Tests ====================

    @Test
    void shouldUpdateCurrentUserSuccessfully() {

        // Arrange
        String email = "mohamed@gmail.com";

        User user = new User(
                "Mohamed",
                "Galhoum",
                email,
                "encodedPassword",
                "01026368065"
        );

        UserUpdateRequest request = new UserUpdateRequest();

        request.setFirstName("Ahmed");
        request.setLastName("Ali");
        request.setPhone("01111111111");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponse response =
                userService.updateCurrentUser(email, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Ahmed");
        assertThat(response.getLastName()).isEqualTo("Ali");
        assertThat(response.getPhone()).isEqualTo("01111111111");

        // Email should remain unchanged
        assertThat(response.getEmail()).isEqualTo(email);

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(user);
    }

    // ==================== Change Password Tests ====================

    @Test
    void shouldChangeCurrentUserPasswordSuccessfully() {

        // Arrange
        String email = "mohamed@gmail.com";

        User user = new User(
                "Mohamed",
                "Galhoum",
                email,
                "encodedOldPassword",
                "01026368065"
        );

        ChangePasswordRequest request = new ChangePasswordRequest();

        request.setCurrentPassword("OldPassword@123");
        request.setNewPassword("NewPassword@123");
        request.setConfirmNewPassword("NewPassword@123");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "OldPassword@123",
                "encodedOldPassword"
        )).thenReturn(true);

        when(passwordEncoder.encode("NewPassword@123"))
                .thenReturn("encodedNewPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        userService.changeCurrentUserPassword(email, request);

        // Assert
        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getPassword())
                .isEqualTo("encodedNewPassword");

        assertThat(savedUser.getTokenVersion())
                .isEqualTo(1);

        verify(passwordEncoder).matches(
                "OldPassword@123",
                "encodedOldPassword"
        );

        verify(passwordEncoder).encode("NewPassword@123");
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {

        // Arrange
        String email = "mohamed@gmail.com";

        User user = new User(
                "Mohamed",
                "Galhoum",
                email,
                "encodedOldPassword",
                "01026368065"
        );

        ChangePasswordRequest request = new ChangePasswordRequest();

        request.setCurrentPassword("WrongPassword@123");
        request.setNewPassword("NewPassword@123");
        request.setConfirmNewPassword("NewPassword@123");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "WrongPassword@123",
                "encodedOldPassword"
        )).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() ->
                userService.changeCurrentUserPassword(email, request))
                .isInstanceOf(InvalidPasswordException.class);

        verify(passwordEncoder).matches(
                "WrongPassword@123",
                "encodedOldPassword"
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordIsSameAsCurrentPassword() {

        // Arrange
        String email = "mohamed@gmail.com";

        User user = new User(
                "Mohamed",
                "Galhoum",
                email,
                "encodedOldPassword",
                "01026368065"
        );

        ChangePasswordRequest request = new ChangePasswordRequest();

        request.setCurrentPassword("OldPassword@123");
        request.setNewPassword("OldPassword@123");
        request.setConfirmNewPassword("OldPassword@123");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "OldPassword@123",
                "encodedOldPassword"
        )).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() ->
                userService.changeCurrentUserPassword(email, request))
                .isInstanceOf(InvalidPasswordException.class);

        verify(passwordEncoder).matches(
                "OldPassword@123",
                "encodedOldPassword"
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldThrowExceptionWhenPasswordConfirmationDoesNotMatch() {

        // Arrange
        String email = "mohamed@gmail.com";

        User user = new User(
                "Mohamed",
                "Galhoum",
                email,
                "encodedOldPassword",
                "01026368065"
        );

        ChangePasswordRequest request = new ChangePasswordRequest();

        request.setCurrentPassword("OldPassword@123");
        request.setNewPassword("NewPassword@123");
        request.setConfirmNewPassword("DifferentPassword@123");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "OldPassword@123",
                "encodedOldPassword"
        )).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() ->
                userService.changeCurrentUserPassword(email, request))
                .isInstanceOf(InvalidPasswordException.class);

        verify(passwordEncoder).matches(
                "OldPassword@123",
                "encodedOldPassword"
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    // ==================== Disable Current User Tests ====================

    @Test
    void shouldDisableCurrentUserSuccessfully() {

        // Arrange
        String email = "mohamed@gmail.com";

        User user = new User(
                "Mohamed",
                "Galhoum",
                email,
                "encodedPassword",
                "01026368065"
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        userService.disableCurrentUser(email);

        // Assert
        assertThat(user.isEnabled()).isFalse();

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenDisablingNonExistingUser() {

        // Arrange
        String email = "notfound@gmail.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() ->
                userService.disableCurrentUser(email))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }
}
