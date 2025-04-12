package com.tenpo.operation.client;

import com.tenpo.operation.client.dto.PercentageResponse;
import com.tenpo.operation.exception.PercentageServiceException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
public class PercentageCircuitBreakerWrapper {

    private final WebClient webClient;

    @Value("${percentage.service.uri}")
    private String percentageServiceUri;
    private static final Logger logger = LoggerFactory.getLogger(PercentageCircuitBreakerWrapper.class);

    public PercentageCircuitBreakerWrapper(WebClient webClient) {
        this.webClient = webClient;
    }

    @CircuitBreaker(name = "percentageService", fallbackMethod = "fallbackPercentage")
    public BigDecimal callExternalPercentage() {
        logger.info("Calling percentage service: {}", percentageServiceUri);
        try {
            PercentageResponse response = webClient
                    .get()
                    .uri(percentageServiceUri)
                    .retrieve()
                    .bodyToMono(PercentageResponse.class)
                    .block();

            assert response != null;
            return BigDecimal.valueOf(response.getPercentage());
        } catch (Exception e) {
            throw new PercentageServiceException("Error fetching percentage from external service");
        }
    }

    public BigDecimal fallbackPercentage(Throwable t) {
        logger.warn("Fallback method called due to: {}", t.getMessage());
        throw new PercentageServiceException("External API failed and circuit breaker activated");
    }
}
