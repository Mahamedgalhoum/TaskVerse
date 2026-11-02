package com.taskverse.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {
    @NotBlank(message = "First Name is Required")
    @Size(max = 50 ,message = "First name must not exceed 50 characters")
    private String firstName;
    @NotBlank(message = "Last Name is Required")
    @Size(max = 50 ,message = "Last name must not exceed 50 characters")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;


}
