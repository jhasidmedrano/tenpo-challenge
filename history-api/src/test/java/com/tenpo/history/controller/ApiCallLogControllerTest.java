package com.tenpo.history.controller;

import com.tenpo.history.dto.ApiCallLogPageResponse;
import com.tenpo.history.dto.PaginationInfo;
import com.tenpo.history.entity.ApiCallLog;
import com.tenpo.history.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApiCallLogControllerTest {

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private ApiCallLogController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void health_shouldReturnOk() {
        var response = controller.health();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getLogs_shouldReturnPaginatedLogs() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        ApiCallLog mockLog = new ApiCallLog();
        mockLog.setId(1L);
        mockLog.setService("history");

        Page<ApiCallLog> mockPage = new PageImpl<>(List.of(mockLog), pageable, 1);

        when(historyService.getAllLogs(pageable)).thenReturn(mockPage);

        var response = controller.getLogs(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiCallLogPageResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getLogs().size());

        PaginationInfo pagination = body.getPagination();
        assertEquals(1, pagination.getTotalElements());
        assertEquals(1, pagination.getTotalPages());
        assertEquals(10, pagination.getPageSize());
        assertTrue(pagination.isFirst());
    }
}
