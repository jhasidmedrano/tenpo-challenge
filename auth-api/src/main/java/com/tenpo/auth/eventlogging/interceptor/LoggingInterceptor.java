package com.tenpo.auth.eventlogging.interceptor;

import com.tenpo.auth.eventlogging.dto.ApiCallLogEvent;
import com.tenpo.auth.eventlogging.filter.RequestResponseCachingFilter;
import com.tenpo.auth.eventlogging.service.KafkaLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private final KafkaLogService kafkaLogService;

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        String requestBody = "";
        String responseBody = "";

        if (request instanceof ContentCachingRequestWrapper reqWrapper) {
            byte[] content = reqWrapper.getContentAsByteArray();
            requestBody = new String(content, StandardCharsets.UTF_8);
        }

        if (response instanceof ContentCachingResponseWrapper resWrapper) {
            byte[] content = resWrapper.getContentAsByteArray();
            responseBody = new String(content, StandardCharsets.UTF_8);
        }

        ApiCallLogEvent log = ApiCallLogEvent.builder()
                .service("auth")
                .method(request.getMethod())
                .endpoint(request.getRequestURI())
                .request(sanitize(requestBody))
                .response(sanitize(responseBody))
                .status(response.getStatus())
                .timestamp(LocalDateTime.now().toString())
                .build();

        kafkaLogService.sendEvent(log);
    }

    private String sanitize(String body) {
        return body
                .replaceAll("\"password\"\\s*:\\s*\"[^\"]+\"", "\"password\":\"***\"")
                .replaceAll("\"token\"\\s*:\\s*\"[^\"]+\"", "\"token\":\"***\"");
    }
}

