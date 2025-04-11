package com.tenpo.operation.service;

import com.tenpo.operation.dto.OperationRequest;

import java.math.BigDecimal;

public interface OperationService {
    BigDecimal sum(OperationRequest request);
}

