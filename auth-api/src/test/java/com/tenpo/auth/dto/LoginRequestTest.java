package com.tenpo.auth.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("john_doe");
        request.setPassword("securePass123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void testBlankUsername() {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("securePass123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for blank username");
    }

    @Test
    void testShortPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("john_doe");
        request.setPassword("123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for short password");
    }

    @Test
    void testNullUsernameAndPassword() {
        LoginRequest request = new LoginRequest();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertEquals(2, violations.size(), "Expected 2 validation errors for null fields");
    }
}
