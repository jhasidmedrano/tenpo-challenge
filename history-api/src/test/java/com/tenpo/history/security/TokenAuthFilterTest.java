package com.tenpo.history.security;

import com.tenpo.history.config.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TokenAuthFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext(); // limpio antes de cada test
    }

    @Test
    void shouldAuthenticateAndContinue_whenTokenIsValid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.jwt.token");
        doNothing().when(jwtUtil).validateToken("valid.jwt.token");

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth.isAuthenticated() || auth.getAuthorities().isEmpty()); // no authorities en este caso

        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).validateToken("valid.jwt.token");
    }

    @Test
    void shouldSkipFilter_whenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void shouldSetErrorAndThrow_whenTokenIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        doThrow(new RuntimeException("Token invalid")).when(jwtUtil).validateToken("invalid.token");

        InsufficientAuthenticationException exception = assertThrows(
                InsufficientAuthenticationException.class,
                () -> filter.doFilterInternal(request, response, filterChain)
        );

        assertEquals("Invalid or expired token", exception.getMessage());
        verify(request).setAttribute(eq("auth_error_message"), eq("Invalid or expired token"));
    }
}
