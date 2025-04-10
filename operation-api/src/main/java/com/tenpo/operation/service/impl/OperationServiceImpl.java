package com.tenpo.operation.service.impl;

import com.tenpo.operation.client.PercentageClient;
import com.tenpo.operation.dto.OperationRequest;
import com.tenpo.operation.exception.PercentageUnavailableException;
import com.tenpo.operation.repository.PercentageCacheRepository;
import com.tenpo.operation.service.OperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {

    private final PercentageClient percentageClient;
    private final PercentageCacheRepository percentageCacheRepository;
    private static final Logger logger = LoggerFactory.getLogger(OperationServiceImpl.class);

    public OperationServiceImpl(PercentageClient percentageClient, PercentageCacheRepository percentageCacheRepository) {
        this.percentageClient = percentageClient;
        this.percentageCacheRepository = percentageCacheRepository;
    }
    @Override
    public BigDecimal sum(OperationRequest request) {
        logger.debug("Calculating sum for request: {}", request);

        Optional<BigDecimal> cachedPercentage = percentageCacheRepository.getCachedPercentage();

        if (cachedPercentage.isPresent()) {
            return calculateSum(request, cachedPercentage.get());
        }

        try {
            BigDecimal fresh = percentageClient.getPercentage();
            percentageCacheRepository.savePercentage(fresh);
            return calculateSum(request, fresh);
        } catch (Exception e) {
            logger.error("Failed to fetch percentage from external service and no cache available", e);
            throw new PercentageUnavailableException("Percentage service unavailable and no cached value exists");
        }
    }

    private BigDecimal calculateSum(OperationRequest request, BigDecimal percentage) {
        BigDecimal sum = request.getNum1().add(request.getNum2());

        BigDecimal multiplier = percentage
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .add(BigDecimal.ONE);

        return sum.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }



}
