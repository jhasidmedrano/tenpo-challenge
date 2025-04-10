package com.tenpo.history.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException {
        logger.warn("Unauthorized request to {}: {}", request.getRequestURI(), authException.getMessage());

        String customMessage = (String) request.getAttribute("auth_error_message");
        String message = (customMessage != null) ? customMessage : authException.getMessage();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", message);

        mapper.writeValue(response.getOutputStream(), body);
    }
}
