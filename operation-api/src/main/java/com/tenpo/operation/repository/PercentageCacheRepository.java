package com.tenpo.operation.repository;

import com.tenpo.operation.config.PercentageCacheProperties;
import com.tenpo.operation.service.impl.OperationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

@Repository
public class PercentageCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final PercentageCacheProperties cacheProperties;

    private static final Logger logger = LoggerFactory.getLogger(PercentageCacheRepository.class);

    public PercentageCacheRepository(RedisTemplate<String, String> redisTemplate, PercentageCacheProperties cacheProperties) {
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }

    public Optional<BigDecimal> getCachedPercentage() {
        String value = redisTemplate.opsForValue().get("percentage");
        if (value != null) {
            return Optional.of(new BigDecimal(value));
        }
        return Optional.empty();
    }

    public void savePercentage(BigDecimal value) {
        logger.info("Saving percentage to cache: {}", value);
        Duration ttl = Duration.ofSeconds(cacheProperties.getTtl());
        redisTemplate.opsForValue().set("percentage", value.toPlainString(), ttl);
    }
}
