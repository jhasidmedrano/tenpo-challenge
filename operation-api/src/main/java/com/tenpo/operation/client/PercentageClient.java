package com.tenpo.operation.client;

import com.tenpo.operation.exception.PercentageServiceException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PercentageClient {

    private final PercentageCircuitBreakerWrapper circuitBreakerWrapper;

    public PercentageClient(PercentageCircuitBreakerWrapper circuitBreakerWrapper) {
        this.circuitBreakerWrapper = circuitBreakerWrapper;
    }

    @Retryable(
            retryFor = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public BigDecimal getPercentage() {
        return circuitBreakerWrapper.callExternalPercentage();
    }

    @Recover
    public void recover(Exception e) {
        throw new PercentageServiceException("External percentage API failed after retries");
    }
}


