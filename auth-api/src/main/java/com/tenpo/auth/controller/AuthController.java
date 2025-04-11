package com.tenpo.auth.controller;

import com.tenpo.auth.dto.LoginRequest;
import com.tenpo.auth.dto.TokenResponse;
import com.tenpo.auth.dto.TokenValidationResponse;
import com.tenpo.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/1/auth")
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Authenticate user and generate token")
    @ApiResponse(responseCode = "200", description = "Token generated successfully")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login request received for user: {}", request.getUsername());
        TokenResponse tokenResponse = authService.authenticate(request);
        logger.info("Token generated for user: {}", request.getUsername());
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "Validate token")
    @ApiResponse(responseCode = "200", description = "Token is valid")
    @ApiResponse(responseCode = "401", description = "Token is invalid")
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validate(@RequestParam("token") String token) {
        logger.debug("Token validation request received");
        boolean valid = authService.validateToken(token);
        String message = valid?"Token is valid":"Token is invalid";
        logger.debug("Token validation result: {}", valid);
        return ResponseEntity.ok(new TokenValidationResponse(valid, message));
    }
}
