package com.tenpo.operation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationRequest {

    @NotNull(message = "num1 is required")
    private BigDecimal num1;

    @NotNull(message = "num2 is required")
    private BigDecimal num2;

}