package com.tenpo.operation.client;

import com.tenpo.operation.client.dto.PercentageResponse;
import com.tenpo.operation.exception.PercentageServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
public class PercentageClient {

    private final WebClient webClient;

    @Value("${percentage.service.uri}")
    private String percentageServiceUri;
    private static final Logger logger = LoggerFactory.getLogger(PercentageClient.class);

    public PercentageClient(WebClient externalWebClient) {
        this.webClient = externalWebClient;
    }

    @Retryable(
        retryFor = {RuntimeException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public BigDecimal getPercentage() {
        logger.debug("Getting percentage from external service: {}", percentageServiceUri);
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
            throw new PercentageServiceException("Error getting percentage");
        }
    }

    @Recover
    public void recover(Exception e) {
        logger.error("All retry attempts to fetch percentage failed: {}", e.getMessage());
        throw new PercentageServiceException("External percentage API failed after retries");
    }

}

