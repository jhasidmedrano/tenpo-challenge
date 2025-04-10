package com.tenpo.history.controller;

import com.tenpo.history.entity.ApiCallLog;
import com.tenpo.history.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1/history")
public class ApiCallLogController {

    private final HistoryService historyService;

    public ApiCallLogController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<ApiCallLog> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return historyService.getAllLogs(pageable);
    }
}

