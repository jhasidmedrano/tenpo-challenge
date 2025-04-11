package com.tenpo.history.dto;

import com.tenpo.history.entity.ApiCallLog;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiCallLogPageResponse {
    private List<ApiCallLog> logs;
    private PaginationInfo pagination;
}