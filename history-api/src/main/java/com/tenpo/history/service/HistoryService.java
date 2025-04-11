package com.tenpo.history.service;

import com.tenpo.history.entity.ApiCallLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {
    Page<ApiCallLog> getAllLogs(Pageable pageable);
}
