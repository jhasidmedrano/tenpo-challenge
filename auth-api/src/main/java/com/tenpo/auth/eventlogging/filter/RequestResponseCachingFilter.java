package com.tenpo.auth.eventlogging.filter;

import com.tenpo.auth.eventlogging.service.KafkaLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestResponseCachingFilter extends OncePerRequestFilter {

    private final KafkaLogService kafkaLogService;

    public RequestResponseCachingFilter(KafkaLogService kafkaLogService) {
        this.kafkaLogService = kafkaLogService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
        String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);

        kafkaLogService.sendEvent("auth",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                requestBody,
                responseBody);

        wrappedResponse.copyBodyToResponse();
    }

}