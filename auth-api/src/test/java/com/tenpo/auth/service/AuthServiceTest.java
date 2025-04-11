package com.tenpo.auth.service;

import com.tenpo.auth.config.JwtUtil;
import com.tenpo.auth.dto.LoginRequest;
import com.tenpo.auth.dto.TokenResponse;
import com.tenpo.auth.entity.User;
import com.tenpo.auth.repository.UserRepository;
import com.tenpo.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("password123");

        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPassword");

        when(userRepository.findUserByUsername("john")).thenReturn(user);
        when(encoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("john")).thenReturn("mocked-token");

        TokenResponse response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("mocked-token", response.getToken());
    }

    @Test
    void authenticate_shouldThrowException_whenUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");

        when(userRepository.findUserByUsername("unknown")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.authenticate(request));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void authenticate_shouldThrowException_whenPasswordDoesNotMatch() {
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPassword");

        when(userRepository.findUserByUsername("john")).thenReturn(user);
        when(encoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.authenticate(request));

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void validateToken_shouldDelegateToJwtUtil() {
        when(jwtUtil.validateToken("token123")).thenReturn(true);
        assertTrue(authService.validateToken("token123"));

        when(jwtUtil.validateToken("invalidToken")).thenReturn(false);
        assertFalse(authService.validateToken("invalidToken"));
    }
}