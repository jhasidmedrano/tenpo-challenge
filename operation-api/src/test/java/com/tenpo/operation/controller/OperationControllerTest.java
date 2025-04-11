package com.tenpo.operation.controller;

import com.tenpo.operation.dto.OperationRequest;
import com.tenpo.operation.dto.OperationResponse;
import com.tenpo.operation.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationControllerTest {

    @Mock
    private OperationService operationService;

    @InjectMocks
    private OperationController operationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void health_shouldReturnOkStatus() {
        ResponseEntity<Void> response = operationController.health();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sum_shouldReturnResultSuccessfully() {
        OperationRequest request = new OperationRequest();
        request.setNum1(BigDecimal.valueOf(10));
        request.setNum2(BigDecimal.valueOf(20));

        when(operationService.sum(request)).thenReturn(BigDecimal.valueOf(30));

        ResponseEntity<OperationResponse> response = operationController.sum(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.valueOf(30), response.getBody().getResult());

        verify(operationService, times(1)).sum(request);
    }
}
