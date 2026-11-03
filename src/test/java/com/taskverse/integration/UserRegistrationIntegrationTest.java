package com.taskverse.integration;

import com.taskverse.dto.request.UserRegisterRequest;
import com.taskverse.entity.User;
import com.taskverse.repository.UserRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserRegistrationIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserRegistrationIntegrationTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    void shouldRegisterUserAndPersistInDatabase() throws Exception {

        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("integration@test.com");
        request.setPassword("Password123");
        request.setPhone("01012345678");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email")
                        .value("integration@test.com"))
                .andExpect(jsonPath("$.data.password").doesNotExist());

        User savedUser = userRepository.findByEmail("integration@test.com")
                .orElseThrow();

        assertThat(savedUser.getId()).isNotNull();

        assertThat(savedUser.getFirstName()).isEqualTo("Mohamed");

        assertThat(savedUser.getLastName()).isEqualTo("Galhoum");

        assertThat(savedUser.getEmail())
                .isEqualTo("integration@test.com");

        assertThat(savedUser.getPassword())
                .isNotEqualTo("Password123");

        assertThat(passwordEncoder.matches(
                "Password123",
                savedUser.getPassword()
        )).isTrue();
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {

        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("duplicate@test.com");
        request.setPassword("Password123");
        request.setPhone("01012345678");

        String requestBody = objectMapper.writeValueAsString(request);

        // First registration
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        // Second registration with the same email
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Email already exists"));

        assertThat(userRepository.findByEmail("duplicate@test.com"))
                .isPresent();
    }

    @Test
    void shouldRegisterUserSuccessfullyWithoutPhone() throws Exception {

        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Ahmed");
        request.setLastName("Ali");
        request.setEmail("no-phone@test.com");
        request.setPassword("Password123");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email")
                        .value("no-phone@test.com"))
                .andExpect(jsonPath("$.data.phone").doesNotExist());

        User savedUser = userRepository.findByEmail("no-phone@test.com")
                .orElseThrow();

        assertThat(savedUser.getPhone()).isNull();
    }
}