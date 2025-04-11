package com.tenpo.history.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic apiLogsTopic() {
        return new NewTopic("api.logs", 1, (short) 1);
    }

    @Bean
    public NewTopic apiLogsDltTopic() {
        return new NewTopic("api.logs.DLT", 1, (short) 1);
    }
}
