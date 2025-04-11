package com.tenpo.history.repository;

import com.tenpo.history.entity.ApiCallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, Long> {
}
