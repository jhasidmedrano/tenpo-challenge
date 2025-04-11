package com.tenpo.history.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Bean
    public NewTopic apiLogsTopic() {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic apiLogsDltTopic() {
        return new NewTopic(topic.concat("DLT"), 1, (short) 1);
    }
}
