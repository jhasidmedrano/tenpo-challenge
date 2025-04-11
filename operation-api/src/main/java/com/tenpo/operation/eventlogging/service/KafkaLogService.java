package com.tenpo.operation.eventlogging.service;

import com.tenpo.operation.eventlogging.dto.ApiCallLogEvent;

public interface KafkaLogService {

    void sendEvent(String service,
                      String method,
                      String endpoint,
                      int status,
                      String request,
                      String response);
}
