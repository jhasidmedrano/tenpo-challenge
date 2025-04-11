package com.tenpo.history.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.history.dto.ApiCallLogEvent;
import com.tenpo.history.entity.ApiCallLog;
import com.tenpo.history.repository.ApiCallLogRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApiLogConsumer {

    private final ApiCallLogRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiLogConsumer(ApiCallLogRepository repository) {
        this.repository = repository;
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000))
    @KafkaListener(topics = "api.logs", groupId = "history-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            ApiCallLogEvent event = objectMapper.readValue(record.value(), ApiCallLogEvent.class);

            ApiCallLog log = ApiCallLog.builder()
                    .service(event.getService())
                    .method(event.getMethod())
                    .endpoint(event.getEndpoint())
                    .request(event.getRequest())
                    .response(event.getResponse())
                    .status(event.getStatus())
                    .timestamp(LocalDateTime.parse(event.getTimestamp()))
                    .build();

            repository.save(log);
        } catch (Exception e) {
            throw new RuntimeException("Error processing message", e);
        }
    }
}
