package com.tenpo.auth.eventlogging.service;

import com.tenpo.auth.eventlogging.dto.ApiCallLogEvent;

public interface KafkaLogService {
    void sendEvent(ApiCallLogEvent log);
}
