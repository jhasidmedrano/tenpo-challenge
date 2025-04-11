package com.tenpo.auth.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        jwtUtil = new JwtUtil();
        Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, "mysecretkeymysecretkeymysecretkey12");
    }

    @Test
    void generateToken_and_validateToken_shouldSucceed() {
        String token = jwtUtil.generateToken("john");

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_shouldFail_forInvalidToken() {
        String invalidToken = "this.is.not.a.valid.token";
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken("tenpo");
        String username = jwtUtil.getUsernameFromToken(token);

        assertEquals("tenpo", username);
    }
}

