package com.taskverse.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionController {

    @GetMapping("/test/email-conflict")
    public void throwEmailAlreadyExistsException() {
        throw new EmailAlreadyExistsException("Email already exists");
    }
    @GetMapping("/test/user-not-found")
    public void throwUserNotFoundException() {
        throw new UserNotFoundException("User not found");
    }

    @GetMapping("/test/invalid-password")
    public void throwInvalidPasswordException() {
        throw new InvalidPasswordException("Invalid password");
    }

    @GetMapping("/test/unexpected-error")
    public void throwUnexpectedException() {
        throw new RuntimeException("Something went wrong");
    }
    @PostMapping("/test/validation")
    public void testValidation(
            @Valid @RequestBody TestRequest request) {
    }
    @Getter
    @Setter
    static class TestRequest {

        @NotBlank(message = "Name is required")
        private String name;
    }
}