package com.tenpo.auth.dto;

import lombok.Data;

@Data
public class TokenValidationResponse {

    private boolean valid;
    private String message;

    public TokenValidationResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
}