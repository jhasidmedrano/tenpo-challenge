package com.tenpo.operation.config;

import com.tenpo.operation.dto.ErrorResponse;
import com.tenpo.operation.exception.PercentageServiceException;
import com.tenpo.operation.exception.PercentageUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleRuntimeException_shouldReturnBadRequest() {
        RuntimeException ex = new RuntimeException("unexpected error");

        ResponseEntity<ErrorResponse> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", Objects.requireNonNull(response.getBody()).getError());
        assertEquals("unexpected error", response.getBody().getMessage());
    }

    @Test
    void handleValidationException_shouldReturnValidationError() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldError()).thenReturn(new FieldError("obj", "field", "Field is invalid"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation Error", Objects.requireNonNull(response.getBody()).getError());
        assertEquals("Field is invalid", response.getBody().getMessage());
    }

    @Test
    void handleUnavailable_shouldReturnServiceUnavailable() {
        PercentageUnavailableException ex = new PercentageUnavailableException("service not reachable");

        ResponseEntity<ErrorResponse> response = handler.handleUnavailable(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Service Unavailable", Objects.requireNonNull(response.getBody()).getError());
        assertEquals("service not reachable", response.getBody().getMessage());
    }

    @Test
    void handlePercentageServiceException_shouldReturnInternalServerError() {
        PercentageServiceException ex = new PercentageServiceException("error from downstream");

        ResponseEntity<ErrorResponse> response = handler.handlePercentageServiceException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Service Unavailable", Objects.requireNonNull(response.getBody()).getError());
        assertEquals("error from downstream", response.getBody().getMessage());
    }
}
