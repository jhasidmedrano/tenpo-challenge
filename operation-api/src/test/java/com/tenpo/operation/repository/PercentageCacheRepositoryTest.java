package com.tenpo.operation.repository;

import com.tenpo.operation.config.PercentageCacheProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PercentageCacheRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private PercentageCacheProperties cacheProperties;

    @InjectMocks
    private PercentageCacheRepository cacheRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void getCachedPercentage_shouldReturnValueIfExists() {
        when(valueOperations.get("percentage")).thenReturn("12.5");

        Optional<BigDecimal> result = cacheRepository.getCachedPercentage();

        assertTrue(result.isPresent());
        assertEquals(BigDecimal.valueOf(12.5), result.get());
    }

    @Test
    void getCachedPercentage_shouldReturnEmptyIfNotFound() {
        when(valueOperations.get("percentage")).thenReturn(null);

        Optional<BigDecimal> result = cacheRepository.getCachedPercentage();

        assertTrue(result.isEmpty());
    }

    @Test
    void savePercentage_shouldStoreValueInRedisWithTTL() {
        BigDecimal value = BigDecimal.valueOf(8.75);
        when(cacheProperties.getTtl()).thenReturn(60L);

        cacheRepository.savePercentage(value);

        verify(valueOperations).set("percentage", "8.75", Duration.ofSeconds(60));
    }
}
