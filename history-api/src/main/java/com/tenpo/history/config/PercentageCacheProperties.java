package com.tenpo.history.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cache.percentage")
public class PercentageCacheProperties {

    private long ttl;
}
