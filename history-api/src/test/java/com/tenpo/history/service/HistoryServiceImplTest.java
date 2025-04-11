package com.tenpo.history.service;

import com.tenpo.history.entity.ApiCallLog;
import com.tenpo.history.repository.ApiCallLogRepository;
import com.tenpo.history.service.impl.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HistoryServiceImplTest {

    @Mock
    private ApiCallLogRepository repository;

    @InjectMocks
    private HistoryServiceImpl historyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLogs_shouldReturnPageOfLogs() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("timestamp").descending());
        ApiCallLog log = new ApiCallLog();
        Page<ApiCallLog> mockPage = new PageImpl<>(Collections.singletonList(log));

        when(repository.findAll(pageable)).thenReturn(mockPage);

        Page<ApiCallLog> result = historyService.getAllLogs(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(log, result.getContent().getFirst());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void getAllLogs_shouldReturnEmptyPageWhenNoLogs() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ApiCallLog> emptyPage = Page.empty();

        when(repository.findAll(pageable)).thenReturn(emptyPage);

        Page<ApiCallLog> result = historyService.getAllLogs(pageable);

        assertEquals(0, result.getContent().size());
        verify(repository).findAll(pageable);
    }
}

