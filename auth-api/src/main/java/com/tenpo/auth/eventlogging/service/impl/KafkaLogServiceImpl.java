package com.tenpo.auth.eventlogging.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.auth.eventlogging.dto.ApiCallLogEvent;
import com.tenpo.auth.eventlogging.service.KafkaLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaLogServiceImpl implements KafkaLogService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TOPIC = "api.logs";
    private static final Logger logger = LoggerFactory.getLogger(KafkaLogServiceImpl.class);

    @Override
    public void sendEvent(ApiCallLogEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, json);
        } catch (Exception e) {
            logger.error("Failed to send log to Kafka", e);
        }
    }
}
