package com.tenpo.operation.eventlogging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallLogEvent {
    private String service;
    private String method;
    private String endpoint;
    private String request;
    private String response;
    private int status;
    private String timestamp;
}
