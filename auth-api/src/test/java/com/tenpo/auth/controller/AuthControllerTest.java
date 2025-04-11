package com.tenpo.auth.controller;

import com.tenpo.auth.dto.LoginRequest;
import com.tenpo.auth.dto.TokenResponse;
import com.tenpo.auth.dto.TokenValidationResponse;
import com.tenpo.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHealth() {
        ResponseEntity<Void> response = authController.health();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("pass");

        TokenResponse mockResponse = new TokenResponse("mock-jwt-token");
        when(authService.authenticate(request)).thenReturn(mockResponse);

        ResponseEntity<TokenResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mock-jwt-token", response.getBody().getToken());
        verify(authService, times(1)).authenticate(request);
    }

    @Test
    public void testValidateToken_Valid() {
        String token = "valid-token";
        when(authService.validateToken(token)).thenReturn(true);

        ResponseEntity<TokenValidationResponse> response = authController.validate(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isValid());
        assertEquals("Token is valid", response.getBody().getMessage());
    }

    @Test
    public void testValidateToken_Invalid() {
        String token = "invalid-token";
        when(authService.validateToken(token)).thenReturn(false);

        ResponseEntity<TokenValidationResponse> response = authController.validate(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isValid());
        assertEquals("Token is invalid", response.getBody().getMessage());
    }
}
