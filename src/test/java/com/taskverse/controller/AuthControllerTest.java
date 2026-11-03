package com.taskverse.controller;

import tools.jackson.databind.ObjectMapper;
import com.taskverse.dto.request.UserRegisterRequest;
import com.taskverse.dto.response.UserResponse;
import com.taskverse.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private final MockMvc mockMvc;
    @MockitoBean
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    AuthControllerTest(
            MockMvc mockMvc,
            UserService userService,
            ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("Password123");
        request.setPhone("01026368065");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFirstName("Mohamed");
        userResponse.setLastName("Galhoum");
        userResponse.setEmail("mohamed@gmail.com");
        userResponse.setPhone("01026368065");

        when(userService.registerUser(any(UserRegisterRequest.class)))
                .thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("User registered successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email")
                        .value("mohamed@gmail.com"));

        verify(userService, times(1))
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("invalid-email");
        request.setPassword("Password123");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenFirstNameIsBlank() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("");
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("Password123");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenLastNameIsBlank() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("Password123");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("12345");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenPhoneNumberIsTooLong() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("Password123");
        request.setPhone("010263680651234567890");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("");
        request.setPassword("Password123");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenEmailIsTooLong() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("a".repeat(90) + "@gmail.com");
        request.setPassword("Password123");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenFirstNameIsTooLong() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("a".repeat(51));
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("Password123");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenLastNameIsTooLong() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("a".repeat(51));
        request.setEmail("mohamed@gmail.com");
        request.setPassword("Password123");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsBlank() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("");
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenPasswordIsTooLong() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("a".repeat(101));
        request.setPhone("01026368065");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
    @Test
    void shouldRegisterUserSuccessfullyWithoutPhone() throws Exception {

        // Arrange
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Mohamed");
        request.setLastName("Galhoum");
        request.setEmail("mohamed@gmail.com");
        request.setPassword("Password123");
        request.setPhone(null);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFirstName("Mohamed");
        userResponse.setLastName("Galhoum");
        userResponse.setEmail("mohamed@gmail.com");
        userResponse.setPhone(null);

        when(userService.registerUser(any(UserRegisterRequest.class)))
                .thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.phone").doesNotExist());

        verify(userService, times(1))
                .registerUser(any(UserRegisterRequest.class));
    }

    @Test
    void shouldReturnUnsupportedMediaTypeWhenContentTypeIsInvalid() throws Exception {

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Invalid Request"))
                .andExpect(status().isUnsupportedMediaType());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsEmpty() throws Exception {

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .registerUser(any(UserRegisterRequest.class));
    }
}