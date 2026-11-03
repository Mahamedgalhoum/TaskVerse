package com.taskverse.service;

import com.taskverse.dto.request.ChangePasswordRequest;
import com.taskverse.dto.request.UserUpdateRequest;
import com.taskverse.exception.InvalidPasswordException;
import com.taskverse.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.taskverse.repository.UserRepository;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.InjectMocks;

import com.taskverse.dto.request.UserRegisterRequest;
import com.taskverse.dto.response.UserResponse;
import com.taskverse.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import java.util.Optional;
import com.taskverse.exception.EmailAlreadyExistsException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();

        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("Mohamed@Gmail.com");
        request.setPassword("TaskVerse@123");
        request.setPhone("01026368065");

        // Mock Repository behavior
        when(userRepository.existsByEmail("mohamed@gmail.com"))
                .thenReturn(false);

        // Mock PasswordEncoder behavior
        when(passwordEncoder.encode("TaskVerse@123"))
                .thenReturn("encodedPassword");

        // Mock save behavior
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.registerUser(request);

        assertThat(response).isNotNull();

        assertThat(response.getFirstName()).isEqualTo("Mohamed");
        assertThat(response.getLastName()).isEqualTo("Galhoum");
        assertThat(response.getEmail()).isEqualTo("mohamed@gmail.com");
        assertThat(response.getPhone()).isEqualTo("01026368065");

        verify(userRepository).existsByEmail("mohamed@gmail.com");

        verify(passwordEncoder).encode("TaskVerse@123");

        verify(userRepository).save(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

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
    @Test
    void shouldGetUserByIdSuccessfully() {
        // Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedPassword",
                "01026368065"
        );
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        UserResponse response = userService.getUserById(1L);
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Mohamed");
        assertThat(response.getLastName()).isEqualTo("Galhoum");
        assertThat(response.getEmail()).isEqualTo("mohamed@gmail.com");
        assertThat(response.getPhone()).isEqualTo("01026368065");

        verify(userRepository).findById(1L);
    }
    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(100L))
                .isInstanceOf(UserNotFoundException.class);

        // Verify
        verify(userRepository).findById(100L);
    }
    @Test
    void shouldUpdateUserSuccessfully() {
// Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedPassword",
                "01026368065"
        );

        UserUpdateRequest request = new UserUpdateRequest();

        request.setFirstName("Ahmed");
        request.setLastName("Ali");
        request.setPhone("01111111111");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(1L, request);
        // Assert
        assertThat(response).isNotNull();

        assertThat(response.getFirstName()).isEqualTo("Ahmed");
        assertThat(response.getLastName()).isEqualTo("Ali");
        assertThat(response.getPhone()).isEqualTo("01111111111");

        // Properties that should remain unchanged
        assertThat(response.getEmail()).isEqualTo("mohamed@gmail.com");

        // Verify
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }
    @Test
    void shouldChangePasswordSuccessfully() {
// Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedOldPassword",
                "01026368065"
        );

        ChangePasswordRequest request = new ChangePasswordRequest();

        request.setCurrentPassword("OldPassword@123");
        request.setNewPassword("NewPassword@123");
        request.setConfirmNewPassword("NewPassword@123");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("OldPassword@123", "encodedOldPassword"))
                .thenReturn(true);

        when(passwordEncoder.encode("NewPassword@123"))
                .thenReturn("encodedNewPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // Act
        userService.changePassword(1L, request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getPassword()).isEqualTo("encodedNewPassword");
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        // Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedOldPassword",
                "01026368065"
        );

        ChangePasswordRequest request = new ChangePasswordRequest();

        request.setCurrentPassword("WrongPassword@123");
        request.setNewPassword("NewPassword@123");
        request.setConfirmNewPassword("NewPassword@123");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("WrongPassword@123", "encodedOldPassword"))
                .thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword(1L, request))
                .isInstanceOf(InvalidPasswordException.class);

        verify(userRepository).findById(1L);

        verify(passwordEncoder).matches("WrongPassword@123", "encodedOldPassword");
    }
    @Test
    void shouldThrowExceptionWhenNewPasswordIsSameAsCurrentPassword() {
        // Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedOldPassword",
                "01026368065"
        );

        ChangePasswordRequest request = new ChangePasswordRequest();

        request.setCurrentPassword("OldPassword@123");
        request.setNewPassword("OldPassword@123");
        request.setConfirmNewPassword("OldPassword@123");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("OldPassword@123", "encodedOldPassword"))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.changePassword(1L, request))
                .isInstanceOf(InvalidPasswordException.class);

        verify(userRepository).findById(1L);

        verify(passwordEncoder).matches("OldPassword@123", "encodedOldPassword");
    }
    @Test
    void shouldThrowExceptionWhenPasswordConfirmationDoesNotMatch() {
        // Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedOldPassword",
                "01026368065"
        );

        ChangePasswordRequest request = new ChangePasswordRequest();

        request.setCurrentPassword("OldPassword@123");
        request.setNewPassword("NewPassword@123");
        request.setConfirmNewPassword("DifferentPassword@123");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("OldPassword@123", "encodedOldPassword"))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.changePassword(1L, request))
                .isInstanceOf(InvalidPasswordException.class);

        verify(userRepository).findById(1L);

        verify(passwordEncoder).matches("OldPassword@123", "encodedOldPassword");
    }
    @Test
    void shouldDeleteUserSuccessfully() {
        // Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedPassword",
                "01026368065"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(1L);

        // Verify
        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        // Arrange
        when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(100L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(100L);
        verify(userRepository, never()).delete(any(User.class));
    }
}
