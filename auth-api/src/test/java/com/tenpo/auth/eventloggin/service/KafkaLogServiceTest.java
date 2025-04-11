package com.tenpo.auth.eventloggin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.auth.eventlogging.dto.ApiCallLogEvent;
import com.tenpo.auth.eventlogging.service.impl.KafkaLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class KafkaLogServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private KafkaLogServiceImpl kafkaLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaLogService = new KafkaLogServiceImpl(kafkaTemplate, objectMapper);
        setKafkaTopic(kafkaLogService, "api.logs");
    }

    private void setKafkaTopic(KafkaLogServiceImpl service, String topicValue) {
        try {
            java.lang.reflect.Field field = KafkaLogServiceImpl.class.getDeclaredField("topic");
            field.setAccessible(true);
            field.set(service, topicValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendEvent_shouldSendSanitizedMessage() throws JsonProcessingException {
        kafkaLogService.sendEvent("auth", "POST", "/login", 200,
                "{\"username\":\"john\",\"password\":\"secret\"}",
                "{\"token\":\"abc123\"}");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(eq("api.logs"), captor.capture());

        String valueSent = captor.getValue();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(valueSent);

        String request = json.get("request").asText();
        String response = json.get("response").asText();

        assertTrue(request.contains("\"password\":\"***\""));
        assertTrue(request.contains("\"username\":\"john\""));
        assertTrue(response.contains("\"token\":\"***\""));

        assertFalse(request.contains("secret"));
        assertFalse(response.contains("abc123"));
    }

    @Test
    void send_shouldHandleSerializationErrorGracefully() throws JsonProcessingException {
        ApiCallLogEvent event = ApiCallLogEvent.builder()
                .service("auth")
                .method("POST")
                .endpoint("/fail")
                .status(500)
                .request("{}")
                .response("{}")
                .timestamp("2025-04-11T00:00:00")
                .build();

        doThrow(new JsonProcessingException("Serialization failed") {}).when(objectMapper).writeValueAsString(any());

        kafkaLogService.sendEvent("auth", "POST", "/fail", 500, "{}", "{}");

        verify(kafkaTemplate, never()).send(any(), any());
    }
}