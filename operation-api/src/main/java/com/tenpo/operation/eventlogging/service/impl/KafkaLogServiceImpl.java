package com.tenpo.operation.eventlogging.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.operation.eventlogging.dto.ApiCallLogEvent;
import com.tenpo.operation.eventlogging.service.KafkaLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaLogServiceImpl implements KafkaLogService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "api.logs";
    private static final Logger logger = LoggerFactory.getLogger(KafkaLogServiceImpl.class);

    @Override
    public void sendEvent(String service, String method, String endpoint, int status, String request, String response) {

        ApiCallLogEvent log = ApiCallLogEvent.builder()
                .service(service)
                .method(method)
                .endpoint(endpoint)
                .status(status)
                .request(sanitize(request))
                .response(sanitize(response))
                .timestamp(LocalDateTime.now().toString())
                .build();

        send(log);
    }

    private void send(ApiCallLogEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, json);
        } catch (Exception e) {
            logger.error("Failed to send log to Kafka", e);
        }
    }
    private String sanitize(String body) {
        if (body == null) return "";

        String sanitized = body
                .replaceAll("\"password\"\\s*:\\s*\"[^\"]+\"", "\"password\":\"***\"")
                .replaceAll("\"token\"\\s*:\\s*\"[^\"]+\"", "\"token\":\"***\"");

        return compactJson(sanitized);
    }

    private String compactJson(String rawJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(rawJson, Object.class);
            return mapper.writeValueAsString(json);
        } catch (Exception e) {
            return rawJson;
        }
    }
}
