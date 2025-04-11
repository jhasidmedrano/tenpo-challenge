package com.tenpo.auth.eventlogging.service;

public interface KafkaLogService {

    void sendEvent(String service,
                      String method,
                      String endpoint,
                      int status,
                      String request,
                      String response);
}
