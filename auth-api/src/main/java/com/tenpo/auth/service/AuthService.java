package com.tenpo.auth.service;

import com.tenpo.auth.dto.LoginRequest;
import com.tenpo.auth.dto.TokenResponse;

public interface AuthService {
    TokenResponse authenticate(LoginRequest request);
    boolean validateToken(String token);
}

