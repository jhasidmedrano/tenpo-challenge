package com.tenpo.operation.service;

import com.tenpo.operation.client.PercentageClient;
import com.tenpo.operation.dto.OperationRequest;
import com.tenpo.operation.exception.PercentageUnavailableException;
import com.tenpo.operation.repository.PercentageCacheRepository;
import com.tenpo.operation.service.impl.OperationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationServiceTest {

    @Mock
    private PercentageClient percentageClient;

    @Mock
    private PercentageCacheRepository percentageCacheRepository;

    @InjectMocks
    private OperationServiceImpl operationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private OperationRequest buildRequest(BigDecimal a, BigDecimal b) {
        OperationRequest request = new OperationRequest();
        request.setNum1(a);
        request.setNum2(b);
        return request;
    }

    @Test
    void sum_shouldUseCachedPercentage() {
        OperationRequest request = buildRequest(BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        BigDecimal percentage = BigDecimal.valueOf(10);

        when(percentageCacheRepository.getCachedPercentage()).thenReturn(Optional.of(percentage));

        BigDecimal result = operationService.sum(request);

        BigDecimal expected = BigDecimal.valueOf(150)
                .multiply(BigDecimal.valueOf(1.10))
                .setScale(2, RoundingMode.HALF_UP );

        assertEquals(expected, result);
        verify(percentageCacheRepository, never()).savePercentage(any());
        verify(percentageClient, never()).getPercentage();
    }

    @Test
    void sum_shouldFetchAndCachePercentageIfNotCached() {
        OperationRequest request = buildRequest(BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        BigDecimal freshPercentage = BigDecimal.valueOf(20);

        when(percentageCacheRepository.getCachedPercentage()).thenReturn(Optional.empty());
        when(percentageClient.getPercentage()).thenReturn(freshPercentage);

        BigDecimal result = operationService.sum(request);

        BigDecimal expected = BigDecimal.valueOf(15)
                .multiply(BigDecimal.valueOf(1.20))
                .setScale(2, RoundingMode.HALF_UP );

        assertEquals(expected, result);
        verify(percentageCacheRepository).savePercentage(freshPercentage);
    }

    @Test
    void sum_shouldThrowExceptionIfNoCacheAndClientFails() {
        OperationRequest request = buildRequest(BigDecimal.TEN, BigDecimal.ONE);

        when(percentageCacheRepository.getCachedPercentage()).thenReturn(Optional.empty());
        when(percentageClient.getPercentage()).thenThrow(new RuntimeException("fail"));

        assertThrows(PercentageUnavailableException.class, () -> operationService.sum(request));
    }
}
