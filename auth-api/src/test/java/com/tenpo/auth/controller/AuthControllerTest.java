package com.tenpo.auth.controller;

import com.tenpo.auth.dto.LoginRequest;
import com.tenpo.auth.dto.TokenResponse;
import com.tenpo.auth.dto.TokenValidationResponse;
import com.tenpo.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void login_shouldReturnTokenResponse() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin");

        TokenResponse expectedResponse = new TokenResponse("mocked-token");

        when(authService.authenticate(request)).thenReturn(expectedResponse);

        ResponseEntity<TokenResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-token", Objects.requireNonNull(response.getBody()).getToken());
    }

    @Test
    void login_shouldReturnErrorResponse() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong-password");

        when(authService.authenticate(request)).thenThrow(new RuntimeException("Invalid credentials"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authController.login(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void validate_shouldReturnTrue() {
        String token = "valid-token";
        when(authService.validateToken(token)).thenReturn(true);

        ResponseEntity<TokenValidationResponse> response = authController.validate(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, Objects.requireNonNull(response.getBody()).isValid());
    }

    @Test
    void validate_shouldReturnFalse() {
        String token = "invalid-token";
        when(authService.validateToken(token)).thenReturn(false);

        ResponseEntity<TokenValidationResponse> response = authController.validate(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.FALSE, Objects.requireNonNull(response.getBody()).isValid());
    }
}
