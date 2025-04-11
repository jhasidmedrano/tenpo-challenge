package com.tenpo.operation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.operation.eventlogging.service.KafkaLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final KafkaLogService kafkaLogService;
    private final ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    public CustomAuthenticationEntryPoint(KafkaLogService kafkaLogService, ObjectMapper mapper) {
        this.kafkaLogService = kafkaLogService;
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
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        body.put("message", message);
        body.put("timestamp", Instant.now());

        mapper.writeValue(response.getOutputStream(), body);

        String responseBody = mapper.writeValueAsString(body);

        kafkaLogService.sendEvent("auth", request.getMethod(), request.getRequestURI(), response.getStatus()
                , "requestBody", responseBody);
    }
}
