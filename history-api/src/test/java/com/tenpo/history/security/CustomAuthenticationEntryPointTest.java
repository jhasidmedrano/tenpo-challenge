package com.tenpo.history.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomAuthenticationEntryPointTest {

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private CustomAuthenticationEntryPoint entryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Captor
    private ArgumentCaptor<Map<String, Object>> responseCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void commence_shouldSetUnauthorizedResponse_withCustomMessage() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/1/history/logs");
        when(request.getAttribute("auth_error_message")).thenReturn("Token expired");
        when(authException.getMessage()).thenReturn("Some default error");

        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        entryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType("application/json");

        verify(mapper).writeValue(eq(outputStream), responseCaptor.capture());

        Map<String, Object> responseBody = responseCaptor.getValue();
        assertEquals(401, responseBody.get("status"));
        assertEquals("Unauthorized", responseBody.get("error"));
        assertEquals("Token expired", responseBody.get("message"));
        assertInstanceOf(Instant.class, responseBody.get("timestamp"));
    }

    @Test
    void commence_shouldSetUnauthorizedResponse_withDefaultMessageIfNoCustomProvided() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/1/history/logs");
        when(request.getAttribute("auth_error_message")).thenReturn(null);
        when(authException.getMessage()).thenReturn("Default error");

        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        entryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(mapper).writeValue(eq(outputStream), responseCaptor.capture());

        Map<String, Object> responseBody = responseCaptor.getValue();
        assertEquals("Default error", responseBody.get("message"));
    }
}
