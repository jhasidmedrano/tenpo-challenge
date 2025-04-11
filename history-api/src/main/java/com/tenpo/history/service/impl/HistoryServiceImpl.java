package com.tenpo.history.service.impl;

import com.tenpo.history.entity.ApiCallLog;
import com.tenpo.history.repository.ApiCallLogRepository;
import com.tenpo.history.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final ApiCallLogRepository repository;

    public HistoryServiceImpl(ApiCallLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ApiCallLog> getAllLogs(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
