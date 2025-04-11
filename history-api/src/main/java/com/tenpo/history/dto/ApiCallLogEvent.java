package com.tenpo.history.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiCallLogEvent {
    private String service;
    private String method;
    private String endpoint;
    private String timestamp;
    private String request;
    private String response;
    private int status;
}
