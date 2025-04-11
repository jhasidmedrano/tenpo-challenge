package com.tenpo.operation.controller;

import com.tenpo.operation.dto.OperationRequest;
import com.tenpo.operation.dto.OperationResponse;
import com.tenpo.operation.service.OperationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Random;

@RestController
@RequestMapping("/api/1/operation")
public class OperationController {

    private final OperationService operationService;
    private static final Logger logger = LoggerFactory.getLogger(OperationController.class);

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        logger.debug("This request is random number generator {}", new Random().nextInt(100));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sum")
    public ResponseEntity<OperationResponse> sum(@Valid @RequestBody OperationRequest request) {
        logger.info("Received sum request: {}", request);

        BigDecimal result = operationService.sum(request);
        OperationResponse response = new OperationResponse();

        response.setResult(result);

        logger.debug("Sum result: {}", result);
        return ResponseEntity.ok(response);
    }
}
