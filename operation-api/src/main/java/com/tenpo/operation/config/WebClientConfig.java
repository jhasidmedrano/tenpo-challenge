package com.tenpo.operation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebClientConfig {

    @Value("${percentage.service.base.url}")
    private String percentageServiceBaseUrl;

    @Bean
    public WebClient externalWebClient() {
        return WebClient.builder()
                .baseUrl(percentageServiceBaseUrl)
                .build();
    }
}
